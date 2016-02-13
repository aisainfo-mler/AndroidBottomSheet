/*
 * AndroidBottomSheet Copyright 2016 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.bottomsheet.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Represents a divider, which can be shown in a bottom sheet.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class Divider extends AbstractItem {

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The divider's title.
     */
    private CharSequence title;

    /**
     * Creates a new divider.
     *
     * @param id
     *         The divider's id as an {@link Integer} value
     */
    public Divider(final int id) {
        super(id);
        this.title = null;
    }

    /**
     * Returns the divider's title.
     *
     * @return The divider's title as an instance of the type {@link CharSequence} or null, if no
     * title has been set
     */
    public final CharSequence getTitle() {
        return title;
    }

    /**
     * Sets the divider's title.
     *
     * @param title
     *         The title, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no title should be set
     */
    public final void setTitle(@Nullable final CharSequence title) {
        this.title = title;
    }

    /**
     * Sets the divider's title.
     *
     * @param context
     *         The context, which should be used, as an instance of the class {@link Context}. The
     *         context may not be null
     * @param resourceId
     *         The resource id of the title, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setTitle(@NonNull final Context context, @StringRes final int resourceId) {
        setTitle(context.getText(resourceId));
    }

    @Override
    public final Divider clone() {
        Divider clonedDivider = new Divider(getId());
        clonedDivider.setTitle(getTitle());
        return clonedDivider;
    }

    @Override
    public final String toString() {
        return "Divider [id=" + getId() + ", title=" + getTitle() + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Divider other = (Divider) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}