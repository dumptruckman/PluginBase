/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.sponge.permission;

import pluginbase.permission.Perm;
import pluginbase.permission.PermDefault;

import java.util.Map;

/**
 * Represents a permissions that a Minecraft player may have.
 * <p/>
 * These should generally be defined as constants.
 * <p/>
 * This class must be implemented for your specific Minecraft Server implementation.  See {@link #verify(String)}.
 * @deprecated This class is not currently required for Sponge implementation.
 */
public class SpongePerm extends Perm {
    
    protected SpongePerm(final Class pluginClass, final String name, final String description,
                         final Map<String, Boolean> children, final PermDefault permDefault, final Map<String, Boolean> parents,
                         final boolean baseName, final boolean specificOnly) {
        super(pluginClass, name, description, children, permDefault, parents, baseName, specificOnly);
        if (!specificOnly) {
            verify(getName());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void verify(final String name) {
    }
}
