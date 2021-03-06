/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.internal.profiles;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.CommonTriggerEvents;
import org.eclipse.smarthome.core.thing.profiles.ProfileCallback;
import org.eclipse.smarthome.core.thing.profiles.TriggerProfile;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 *
 * @author Simon Kaufmann - initial contribution and API.
 *
 */
public class RawButtonToggleSwitchProfileTest {

    @Mock
    private ProfileCallback mockCallback;

    @Before
    public void setup() {
        mockCallback = mock(ProfileCallback.class);
    }

    @Test
    public void testSwitchItem() {
        TriggerProfile profile = new RawButtonToggleSwitchProfile(mockCallback);
        verifyAction(profile, UnDefType.NULL, OnOffType.ON);
        verifyAction(profile, OnOffType.ON, OnOffType.OFF);
        verifyAction(profile, OnOffType.OFF, OnOffType.ON);
    }

    @Test
    public void testColorItem() {
        TriggerProfile profile = new RawButtonToggleSwitchProfile(mockCallback);
        verifyAction(profile, UnDefType.NULL, OnOffType.ON);
        verifyAction(profile, HSBType.WHITE, OnOffType.OFF);
        verifyAction(profile, HSBType.BLACK, OnOffType.ON);
    }

    private void verifyAction(TriggerProfile profile, State preCondition, Command expectation) {
        reset(mockCallback);
        profile.onTriggerFromHandler(CommonTriggerEvents.PRESSED);
        verify(mockCallback, times(1)).sendCommand(eq(expectation));
    }

}
