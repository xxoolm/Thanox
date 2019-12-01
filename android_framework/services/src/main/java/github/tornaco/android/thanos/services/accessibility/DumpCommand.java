/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.android.thanos.services.accessibility;

import android.app.UiAutomation;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.concurrent.TimeoutException;

import github.tornaco.android.thanos.core.util.Timber;

public class DumpCommand {

    public void run() {
        UiAutomationShellWrapper automationWrapper = new UiAutomationShellWrapper();
        automationWrapper.connect();
        automationWrapper.setCompressedLayoutHierarchy(false);
        // It appears that the bridge needs time to be ready. Making calls to the
        // bridge immediately after connecting seems to cause exceptions. So let's also
        // do a wait for idle in case the app is busy.
        try {
            UiAutomation uiAutomation = automationWrapper.getUiAutomation();
            uiAutomation.waitForIdle(100, 1000);
            AccessibilityNodeInfo info = uiAutomation.getRootInActiveWindow();
            if (info == null) {
                Timber.e("ERROR: null root node returned by UiTestAutomationBridge.");
                return;
            }
            Timber.d("AccessibilityNodeInfo: %s", info);
        } catch (TimeoutException re) {
            Timber.e("ERROR: could not get idle state.");
        } finally {
            automationWrapper.disconnect();
        }
    }
}
