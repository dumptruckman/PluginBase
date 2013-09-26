/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.logging.Logging;
import pluginbase.messages.Message;
import pluginbase.properties.ListProperty;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class DefaultListProperty<T> extends DefaultValueProperty<T> implements ListProperty<T> {

    @NotNull
    private final Class<? extends List> listClass;
    @Nullable
    private final List<T> defList;

    public DefaultListProperty(@NotNull final Class<T> type, @NotNull final String path,
                               @Nullable final List<T> def, @NotNull final List<String> comments,
                               @NotNull final List<String> aliases, @Nullable final PropertySerializer<T> serializer,
                               @NotNull final PropertyValidator<T> validator, Message description,
                               boolean deprecated, boolean defaultIfMissing,
                               @NotNull final Class<? extends List> listClass) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
        this.listClass = listClass;
        if (def == null) {
            this.defList = null;
        } else {
            this.defList = Collections.unmodifiableList(def);
        }
    }

    @Nullable
    @Override
    public List<T> getDefault() {
        if (defList == null) {
            return null;
        }
        List<T> list = getNewTypeList();
        list.addAll(defList);
        return list;
    }

    @NotNull
    @Override
    public List<T> getNewTypeList() {
        try {
            @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
            final List<T> list = listClass.newInstance();
            return list;
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        }
        return new ArrayList<T>();
    }
}
