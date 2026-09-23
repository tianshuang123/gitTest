package com.tianshuang.miaoxin

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.max

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { SalaryScreen(this) } }
    }
}

private data class WorkConfig(
    val start: LocalTime = LocalTime.of(9, 0),
    val lunchStart: LocalTime = LocalTime.of(12, 0),
    val lunchEnd: LocalTime = LocalTime.of(13, 0),
    val end: LocalTime = LocalTime.of(18, 0),
    val deductLunch: Boolean = true
)

private val holidays2026 = setOf(
    "2026-01-01","2026-01-02","2026-01-03",
    "2026-02-15","2026-02-16","2026-02-17","2026-02-18","2026-02-19","2026-02-20","2026-02-21","2026-02-22","2026-02-23",
    "2026-04-04","2026-04-05","2026-04-06",
    "2026-05-01","2026-05-02","2026-05-03","2026-05-04","2026-05-05",
    "2026-06-19","2026-06-20","2026-06-21",
    "2026-09-25","2026-09-26","2026-09-27",
    "2026-10-01","2026-10-02","2026-10-03","2026-10-04","2026-10-05","2026-10-06","2026-10-07"
)
private val makeup2026 = setOf("2026-02-14","2026-02-28","2026-05-09","2026-09-20","2026-10-10")

private fun baseWorkday(date: LocalDate): Boolean {
    val s = date.toString()
    if (s in makeup2026) return true
    if (s in holidays2026) return false
    return date.dayOfWeek !in setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
}

private fun monthWorkdays(month: YearMonth): Int =
    (1..month.lengthOfMonth()).count { baseWorkday(month.atDay(it)) }

private fun eventOverride(context: Context, date: LocalDate): Boolean? {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) return null
    val start = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val uri = CalendarContract.Instances.CONTENT_URI.buildUpon().apply {
        ContentResolver.appendId(this, start)
        ContentResolver.appendId(this, end)
    }.build()
    val projection = arrayOf(CalendarContract.Instances.TITLE)
    runCatching {
        context.contentResolver.query(uri, projection, null, null, null)?.use { c ->
            while (c.moveToNext()) {
                val title = (c.getString(0) ?: "").lowercase()
                if (listOf("请假","年假","病假","事假","休假","leave","vacation").any { title.contains(it) }) return false
                if (listOf("补班","上班","值班","work").any { title.contains(it) }) return true
            }
        }
    }
    return null
}

private fun effectiveWorkday(context: Context, date: LocalDate, readCalendar: Boolean): Boolean =
    if (readCalendar) eventOverride(context, date) ?: baseWorkday(date) else baseWorkday(date)

private fun secondsBetween(a: LocalTime, b: LocalTime) = max(0, ChronoUnit.SECONDS.between(a, b))

private fun dailySeconds(cfg: WorkConfig): Long {
    val total = secondsBetween(cfg.start, cfg.end)
    val lunch = if (cfg.deductLunch) secondsBetween(cfg.lunchStart, cfg.lunchEnd) else 0
    return max(1, total - lunch)
}

private fun workedSeconds(now: LocalTime, cfg: WorkConfig): Long {
    if (now <= cfg.start) return 0
    if (now >= cfg.end) return dailySeconds(cfg)
    var s = secondsBetween(cfg.start, now)
    if (cfg.deductLunch) {
        if (now > cfg.lunchEnd) s -= secondsBetween(cfg.lunchStart, cfg.lunchEnd)
        else if (now > cfg.lunchStart) s -= secondsBetween(cfg.lunchStart, now)
    }
    return s.coerceIn(0, dailySeconds(cfg))
}

private fun formatDuration(seconds: Long): String {
    val s = max(0, seconds)
    val h = s / 3600
    val m = (s % 3600) / 60
    val sec = s % 60
    return "%02d:%02d:%02d".format(h, m, sec)
}

@Composable
private fun SalaryScreen(context: Context) {
    val prefs = remember { context.getSharedPreferences("miaoxin", Context.MODE_PRIVATE) }
    var salary by remember { mutableStateOf(prefs.getString("salary", "10000") ?: "10000") }
    var startText by remember { mutableStateOf(prefs.getString("start", "09:00") ?: "09:00") }
    var lunchStartText by remember { mutableStateOf(prefs.getString("lunchStart", "12:00") ?: "12:00") }
    var lunchEndText by remember { mutableStateOf(prefs.getString("lunchEnd", "13:00") ?: "13:00") }
    var endText by remember { mutableStateOf(prefs.getString("end", "18:00") ?: "18:00") }
    var deductLunch by remember { mutableStateOf(prefs.getBoolean("deductLunch", true)) }
    var readCalendar by remember { mutableStateOf(prefs.getBoolean("readCalendar", false)) }
    var calendarGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
    }
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1000)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        calendarGranted = granted
        if (!granted) readCalendar = false
        prefs.edit().putBoolean("readCalendar", readCalendar && granted).apply()
    }

    val parseTime: (String, LocalTime) -> LocalTime = { txt, fallback ->
        runCatching { LocalTime.parse(txt) }.getOrElse { fallback }
    }

    val cfg = WorkConfig(
        parseTime(startText, LocalTime.of(9, 0)),
        parseTime(lunchStartText, LocalTime.of(12, 0)),
        parseTime(lunchEndText, LocalTime.of(13, 0)),
        parseTime(endText, LocalTime.of(18, 0)),
        deductLunch
    )

    val monthlySalary = salary.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val month = YearMonth.from(now)
    val workdays = monthWorkdays(month)
    val perDay = if (workdays > 0) monthlySalary / workdays else 0.0
    val perSecond = perDay / dailySeconds(cfg)
    val todayWorkday = effectiveWorkday(context, now.toLocalDate(), readCalendar && calendarGranted)
    val earned = if (todayWorkday) workedSeconds(now.toLocalTime(), cfg) * perSecond else 0.0

    val status = when {
        !todayWorkday -> "今天休息" to "不用倒计时，安心休息"
        now.toLocalTime() < cfg.start -> "距离上班" to formatDuration(secondsBetween(now.toLocalTime(), cfg.start))
        now.toLocalTime() >= cfg.end -> "已下班" to "今天辛苦了"
        cfg.deductLunch && now.toLocalTime() >= cfg.lunchStart && now.toLocalTime() < cfg.lunchEnd ->
            "午休中 · 距离下班" to formatDuration(secondsBetween(now.toLocalTime(), cfg.end))
        else -> "距离下班" to formatDuration(secondsBetween(now.toLocalTime(), cfg.end))
    }

    Scaffold { pad ->
        Column(
            Modifier.padding(pad).padding(18.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("秒薪计算器", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "${now.toLocalDate()}  ${now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(status.first, style = MaterialTheme.typography.titleMedium)
                    Text(status.second, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Text("今日已赚", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("¥ %.2f".format(earned), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text("+ ¥ %.6f / 秒".format(perSecond))
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("本月工作日", "$workdays 天", Modifier.weight(1f))
                StatCard("每天工资", "¥ %.2f".format(perDay), Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("每小时", "¥ %.2f".format(perSecond * 3600), Modifier.weight(1f))
                StatCard("每分钟", "¥ %.3f".format(perSecond * 60), Modifier.weight(1f))
            }

            Text("工资与工时", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                salary,
                {
                    salary = it.filter { ch -> ch.isDigit() || ch == '.' }
                    prefs.edit().putString("salary", salary).apply()
                },
                label = { Text("月薪（元）") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeField("上班", startText, { startText = it; prefs.edit().putString("start", it).apply() }, Modifier.weight(1f))
                TimeField("下班", endText, { endText = it; prefs.edit().putString("end", it).apply() }, Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeField("午休开始", lunchStartText, { lunchStartText = it; prefs.edit().putString("lunchStart", it).apply() }, Modifier.weight(1f))
                TimeField("午休结束", lunchEndText, { lunchEndText = it; prefs.edit().putString("lunchEnd", it).apply() }, Modifier.weight(1f))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(deductLunch, {
                    deductLunch = it
                    prefs.edit().putBoolean("deductLunch", it).apply()
                })
                Spacer(Modifier.width(10.dp))
                Text("午休时间不计工资")
            }

            Text("日历", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(readCalendar && calendarGranted, { checked ->
                    if (checked && !calendarGranted) permissionLauncher.launch(Manifest.permission.READ_CALENDAR)
                    else {
                        readCalendar = checked
                        prefs.edit().putBoolean("readCalendar", checked).apply()
                    }
                })
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("读取系统日历")
                    Text(
                        "识别请假、年假、补班、值班等事件",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                "已内置 2026 年中国法定节假日和调休补班规则；系统日历事件可覆盖当天状态。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TimeField(
    label: String,
    value: String,
    onValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value,
        { onValue(it.take(5)) },
        label = { Text(label) },
        placeholder = { Text("09:00") },
        singleLine = true,
        modifier = modifier
    )
}
