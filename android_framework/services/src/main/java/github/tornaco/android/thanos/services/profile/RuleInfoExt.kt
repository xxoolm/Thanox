package github.tornaco.android.thanos.services.profile

import github.tornaco.android.thanos.core.profile.RuleInfo
import org.jeasy.rules.api.Rule

data class RuleInfoExt(val ruleInfo: RuleInfo, val rule: Rule)
