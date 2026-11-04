package in.postal.sorting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.Normalizer
import java.util.Locale

data class RouteRecord(val village: String, val so: String, val bo: String, val pin: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = assets.open("villages.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<RouteRecord>>() {}.type
        val records: List<RouteRecord> = Gson().fromJson(json, type)
        setContent { PostalSearchScreen(records) }
    }
}

private fun norm(s: String) = Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
    .replace("\\p{Mn}+".toRegex(), "").lowercase(Locale.ROOT)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostalSearchScreen(records: List<RouteRecord>) {
    var query by remember { mutableStateOf("") }
    val matches = remember(query, records) {
        if (query.isBlank()) emptyList() else records.filter {
            norm(it.village).contains(norm(query)) ||
                norm(it.so).contains(norm(query)) ||
                norm(it.bo).contains(norm(query)) ||
                it.pin.contains(query.trim())
        }.sortedWith(compareBy({ norm(it.village) }, { norm(it.so) }, { norm(it.bo) }, { it.pin }))
    }
    val duplicateGroups = remember(matches) {
        matches.groupBy { norm(it.village) }.filterValues { it.size > 1 }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Postal Sorting Assistant", fontWeight = FontWeight.SemiBold)
                            Text("Developed by Mahesh Gupta", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                )
            }
        ) { pad ->
            Column(
                Modifier.fillMaxSize().padding(pad).padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text("Village / Hamlet Search", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Village, SO, BO or PIN") },
                    placeholder = { Text("Start typing to search") },
                    singleLine = true,
                    trailingIcon = {
                        if (query.isNotEmpty()) TextButton(onClick = { query = "" }) { Text("Clear") }
                    }
                )
                Spacer(Modifier.height(8.dp))
                if (query.isBlank()) {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Search postal routes", fontWeight = FontWeight.SemiBold)
                            Text("Search using a village / hamlet name, Sub Office, Branch Office or PIN code.", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(6.dp))
                            Text("Offline search • ${records.size} imported route records", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                } else {
                    Text(
                        "${matches.size} matching route(s)  •  ${duplicateGroups.size} duplicate-name group(s)",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(6.dp))
                    if (duplicateGroups.isNotEmpty()) {
                        Text(
                            "Duplicate names found: verify SO / BO before sorting.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    if (matches.isEmpty()) {
                        Text("No match found. Check spelling or ask the administrator to update the master list.", modifier = Modifier.padding(top = 16.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(matches) { item ->
                        val sameName = matches.count { norm(it.village) == norm(item.village) }
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp)) {
                                Text(item.village, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text("SO: ${item.so}")
                                Text("BO: ${item.bo}")
                                Text("PIN: ${item.pin}")
                                if (sameName > 1) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        "Duplicate village name — confirm the SO and BO route.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Data note: currently includes 704 records extracted from the Classified List workbook only. Consolidated.xlsx and official route verification are still pending. Do not treat this draft as the complete operational master.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
