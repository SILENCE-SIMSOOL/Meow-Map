package silence.simsool.meowmap.utils

import com.google.gson.JsonParser
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import silence.simsool.meowmap.kotlin.getJsonArray
import silence.simsool.meowmap.kotlin.getJsonObject
import silence.simsool.meowmap.kotlin.getJsonPrimitive
import silence.simsool.meowmap.kotlin.toJsonObject

object APIUtils {
    fun fetch(uri: String): String? {
        HttpClients.createMinimal().use {
            try {
                val httpGet = HttpGet(uri)
                return EntityUtils.toString(it.execute(httpGet).entity)
            } catch (e: Exception) {
                return null
            }
        }
    }

    fun getSecrets(uuid: String): Int {
        return 0
    }

    fun hasBonusPaulScore(): Boolean {
        val response = fetch("https://api.hypixel.net/resources/skyblock/election") ?: return false
        val jsonObject = JsonParser().parse(response).toJsonObject() ?: return false
        if (jsonObject.getJsonPrimitive("success")?.asBoolean == true) {
            val mayor = jsonObject.getJsonObject("mayor") ?: return false
            val name = mayor.getJsonPrimitive("name")?.asString
            if (name == "Paul") {
                return mayor.getJsonArray("perks")?.any {
                    it.toJsonObject()?.getJsonPrimitive("name")?.asString == "EZPZ"
                } ?: false
            }
        }
        return false
    }
}
