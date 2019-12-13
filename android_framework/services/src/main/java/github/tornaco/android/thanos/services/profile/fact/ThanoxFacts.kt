package github.tornaco.android.thanos.services.profile.fact

import android.content.ComponentName
import github.tornaco.android.thanos.core.util.Timber
import org.jeasy.rules.api.Facts
import java.io.Serializable

class ThanoxFacts : Serializable {

    var pkgAdded = false
    var pkgRemoved = false
    var pkgName: String? = null
    var frontPkgChanged = false
    var from: String? = null
    var to: String? = null
    var taskRemoved = false
    var userId: Int? = null
    var componentName: ComponentName? = null
    var componentNameAsString: String? = null
    var componentNameAsShortString: String? = null
    var activityResumed = false
    var pkgKilled = false

    fun compose(): Facts {
        val facts = Facts()
        val thanoxFactsClass = ThanoxFacts::class.java
        for (field in thanoxFactsClass.declaredFields) {
            val fieldName = field.name
            try {
                val fieldValue = field[this]
                facts.put(fieldName, fieldValue)
                Timber.v("toFacts, add field %s %s", fieldName, fieldValue)
            } catch (e: IllegalAccessException) {
                Timber.e(e, "Error catch while get field value: $field")
            }
        }
        return facts
    }
}