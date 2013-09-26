/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages.messaging;

import pluginbase.messages.Localizable;

/**
 * Indicates that this class has a Messager available for sending messages to users.
 */
public interface Messaging extends Localizable {

    /**
     * Gets the messager.
     *
     * @return the messager.
     */
    Messager getMessager();
}
