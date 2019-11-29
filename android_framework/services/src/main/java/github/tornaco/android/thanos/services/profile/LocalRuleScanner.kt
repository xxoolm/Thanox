@file:Suppress("UnstableApiUsage")

package github.tornaco.android.thanos.services.profile

import com.google.common.io.Files
import github.tornaco.android.thanos.core.profile.ProfileManager
import github.tornaco.android.thanos.core.profile.RuleInfo
import github.tornaco.android.thanos.core.util.FileUtils
import github.tornaco.android.thanos.core.util.Timber
import org.jeasy.rules.mvel.MVELRuleFactory
import org.jeasy.rules.support.JsonRuleDefinitionReader
import org.jeasy.rules.support.YamlRuleDefinitionReader
import java.io.File
import java.io.FileReader
import java.util.*

internal class LocalRuleScanner {
    private val ruleFactoryJson = MVELRuleFactory(JsonRuleDefinitionReader())
    private val ruleFactoryYaml = MVELRuleFactory(YamlRuleDefinitionReader())

    // name-rule
    fun getRulesUnder(dir: File): Map<String, RuleInfoExt> {
        val res = HashMap<String, RuleInfoExt>()
        if (!dir.exists()) return res
        if (!dir.isDirectory) return res
        val subFiles = Files.fileTreeTraverser().postOrderTraversal(dir)
        for (f in subFiles) {
            if (f.isDirectory) continue
            val format = detectFormat(f) ?: continue
            Timber.v("Parse file to rule: %s", f)
            try {
                val rule = detectFactory(format).createRule(FileReader(f))
                Timber.v("Found rule: %s", rule)
                val infoExt = RuleInfoExt(
                    RuleInfo(
                        rule.name,
                        rule.description,
                        FileUtils.readString(f.absolutePath),
                        "tornaco",
                        f.lastModified(),
                        false,
                        format
                    ),
                    rule
                )
                res[rule.name] = infoExt
            } catch (e: Exception) {
                Timber.e(e, "Error parse file to rule: $f")
            }

        }
        return res
    }

    private fun detectFormat(f: File): Int? {
        val ext = Files.getFileExtension(f.absolutePath)
        if (ext.contains("json")) return ProfileManager.RULE_FORMAT_JSON
        if (ext.contains("yml")) return ProfileManager.RULE_FORMAT_YAML
        return null
    }

    private fun detectFactory(format: Int): MVELRuleFactory {
        when (format) {
            ProfileManager.RULE_FORMAT_JSON -> return ruleFactoryJson
            ProfileManager.RULE_FORMAT_YAML -> return ruleFactoryYaml
        }
        throw IllegalArgumentException("Bad format: $format")
    }
}
