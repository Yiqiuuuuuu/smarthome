/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.internal.profiles;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.CoreItemFactory;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.DefaultSystemChannelTypeProvider;
import org.eclipse.smarthome.core.thing.profiles.Profile;
import org.eclipse.smarthome.core.thing.profiles.ProfileAdvisor;
import org.eclipse.smarthome.core.thing.profiles.ProfileCallback;
import org.eclipse.smarthome.core.thing.profiles.ProfileContext;
import org.eclipse.smarthome.core.thing.profiles.ProfileFactory;
import org.eclipse.smarthome.core.thing.profiles.ProfileType;
import org.eclipse.smarthome.core.thing.profiles.ProfileTypeProvider;
import org.eclipse.smarthome.core.thing.profiles.ProfileTypeUID;
import org.eclipse.smarthome.core.thing.profiles.SystemProfiles;
import org.osgi.service.component.annotations.Component;

/**
 * A factory and advisor for default profiles.
 *
 * This {@link ProfileAdvisor} and {@link ProfileFactory} implementation handles all default {@link Profile}s.
 * It will be used as an advisor if the link is not configured and no other advisor returned a result (in that order).
 * The same applies to the creation of profile instances: This factory will be used of no other factory supported the
 * required profile type.
 *
 * @author Simon Kaufmann - initial contribution and API.
 *
 */
@NonNullByDefault
@Component(service = SystemProfileFactory.class)
public class SystemProfileFactory implements ProfileFactory, ProfileAdvisor, ProfileTypeProvider {

    private static final Set<ProfileType> SUPPORTED_PROFILE_TYPES = Stream
            .of(SystemProfiles.DEFAULT_TYPE, SystemProfiles.FOLLOW_TYPE, SystemProfiles.RAWBUTTON_TOGGLE_SWITCH_TYPE)
            .collect(Collectors.toSet());

    private static final Set<ProfileTypeUID> SUPPORTED_PROFILE_TYPE_UIDS = Stream
            .of(SystemProfiles.DEFAULT, SystemProfiles.FOLLOW, SystemProfiles.RAWBUTTON_TOGGLE_SWITCH)
            .collect(Collectors.toSet());

    @Nullable
    @Override
    public Profile createProfile(ProfileTypeUID profileTypeUID, ProfileCallback callback, ProfileContext context) {
        if (SystemProfiles.DEFAULT.equals(profileTypeUID)) {
            return new SystemDefaultProfile(callback);
        } else if (SystemProfiles.FOLLOW.equals(profileTypeUID)) {
            return new SystemFollowProfile(callback);
        } else if (SystemProfiles.RAWBUTTON_TOGGLE_SWITCH.equals(profileTypeUID)) {
            return new RawButtonToggleSwitchProfile(callback);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public ProfileTypeUID getSuggestedProfileTypeUID(Channel channel, @Nullable String itemType) {
        switch (channel.getKind()) {
            case STATE:
                return SystemProfiles.DEFAULT;
            case TRIGGER:
                if (DefaultSystemChannelTypeProvider.SYSTEM_RAWBUTTON.getUID().equals(channel.getChannelTypeUID())) {
                    if (CoreItemFactory.SWITCH.equalsIgnoreCase(itemType)) {
                        return SystemProfiles.RAWBUTTON_TOGGLE_SWITCH;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported channel kind: " + channel.getKind());
        }
        return null;
    }

    @Override
    public Collection<ProfileType> getProfileTypes(@Nullable Locale locale) {
        return SUPPORTED_PROFILE_TYPES;
    }

    @Override
    public @NonNull Collection<@NonNull ProfileTypeUID> getSupportedProfileTypeUIDs() {
        return SUPPORTED_PROFILE_TYPE_UIDS;
    }

}
