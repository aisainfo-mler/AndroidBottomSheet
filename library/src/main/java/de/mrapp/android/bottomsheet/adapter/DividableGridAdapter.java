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
package de.mrapp.android.bottomsheet.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.mrapp.android.bottomsheet.BottomSheet;
import de.mrapp.android.bottomsheet.R;
import de.mrapp.android.bottomsheet.model.Divider;
import de.mrapp.android.bottomsheet.model.Item;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An adapter, which manages the items of a {@link BottomSheet}. It allows to show the items in a
 * list or grid and supports to show dividers.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class DividableGridAdapter extends BaseAdapter {

    /**
     * The view holder, which is used to visualize items.
     */
    private static class ItemViewHolder {

        /**
         * The image view, which is used to show an item's icon.
         */
        private ImageView iconImageView;

        /**
         * The text view, which is used to show an item's title.
         */
        private TextView titleTextView;

    }

    /**
     * The view holder, which is used to visualize dividers.
     */
    private static class DividerViewHolder {

        /**
         * The view, which is used to show the left divider.
         */
        private View leftDivider;

        /**
         * The view, which is used to show the right divider.
         */
        private View rightDivider;

        /**
         * The text view, which is used to show a divider's title.
         */
        private TextView titleTextView;

    }

    /**
     * The view type, which is used to visualize placeholders.
     */
    private static final int PLACEHOLDER_VIEW_TYPE = 0;

    /**
     * The view type, which is used to visualize items.
     */
    private static final int ITEM_VIEW_TYPE = 1;

    /**
     * The view type, which is used to visualize dividers.
     */
    private static final int SEPARATOR_VIEW_TYPE = 2;

    /**
     * The context, which is used by the adapter.
     */
    private final Context context;

    /**
     * A list, which contains the items of the adapter.
     */
    private List<Parcelable> items;

    /**
     * True, if the <code>notifyDataSetChange</code>-method is called automatically, when the
     * adapter's items are changed, false otherwise.
     */
    private boolean notifyOnChange;

    /**
     * The text color of the adapter's items.
     */
    private int itemColor;

    /**
     * The color of the adapter's dividers.
     */
    private int dividerColor;

    /**
     * Notifies, that the adapter's items have been changed.
     */
    private void notifyOnDataSetChanged() {
        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * Inflates the view, which is used to visualize an item.
     *
     * @param parent
     *         The parent of the view, which should be inflated, as an instance of the class {@link
     *         ViewGroup} or null, if no parent is available
     * @return The view, which has been inflated, as an instance of the class {@link View}
     */
    private View inflateItemView(@Nullable final ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder();
        viewHolder.iconImageView = (ImageView) view.findViewById(android.R.id.icon);
        viewHolder.titleTextView = (TextView) view.findViewById(android.R.id.title);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Visualizes a specific item.
     *
     * @param item
     *         The item, which should be visualized, as an instance of the class {@link Item}. The
     *         item may not be null
     * @param viewHolder
     *         The view holder, which contains the views, which should be used to visualize the
     *         item, as an instance of the class {@link ItemViewHolder}. The view holder may not be
     *         null
     */
    private void visualizeItem(@NonNull final Item item, @NonNull final ItemViewHolder viewHolder) {
        viewHolder.titleTextView.setText(item.getTitle());

        if (getItemColor() != -1) {
            viewHolder.titleTextView.setTextColor(getItemColor());
        }
    }

    /**
     * Inflates the view, which is used to visualize a divider.
     *
     * @param parent
     *         The parent of the view, which should be inflated, as an instance of the class {@link
     *         ViewGroup} or null, if no parent is available
     * @return The view, which has been inflated, as an instance of the class {@link View}
     */
    private View inflateDividerView(@Nullable final ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.divider, parent, false);
        DividerViewHolder viewHolder = new DividerViewHolder();
        viewHolder.leftDivider = view.findViewById(R.id.left_divider);
        viewHolder.rightDivider = view.findViewById(R.id.right_divider);
        viewHolder.titleTextView = (TextView) view.findViewById(android.R.id.title);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Visualizes a specific divider.
     *
     * @param divider
     *         The divider, which should be visualized, as an instance of the class {@link Divider}.
     *         The divider may not be null
     * @param viewHolder
     *         The view holder, which contains the views, which should be used to visualize the
     *         divider, as an instance of the class {@link DividerViewHolder}. The view holder may
     *         not be null
     */
    private void visualizeDivider(@NonNull final Divider divider,
                                  @NonNull final DividerViewHolder viewHolder) {
        if (!TextUtils.isEmpty(divider.getTitle())) {
            viewHolder.titleTextView.setText(divider.getTitle());
            viewHolder.titleTextView.setVisibility(View.VISIBLE);
            viewHolder.leftDivider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.titleTextView.setVisibility(View.GONE);
            viewHolder.leftDivider.setVisibility(View.GONE);
        }

        if (dividerColor != -1) {
            viewHolder.titleTextView.setTextColor(dividerColor);
            viewHolder.leftDivider.setBackgroundColor(dividerColor);
            viewHolder.rightDivider.setBackgroundColor(dividerColor);
        }
    }

    /**
     * Creates a new adapter, which manages the items of a {@link BottomSheet}.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public DividableGridAdapter(@NonNull final Context context) {
        ensureNotNull(context, "The context may not be null");
        this.context = context;
        this.items = new ArrayList<>();
        this.notifyOnChange = true;
        this.itemColor = -1;
        this.dividerColor = -1;
        items.add(new Item("Item 1"));
        items.add(new Item("Item 2"));
        items.add(new Item("Item 3"));
        items.add(new Divider());
        items.add(new Item("Item 4"));
        items.add(new Item("Item 5"));
        Divider divider = new Divider();
        divider.setTitle("Divider");
        items.add(divider);
        items.add(new Item("Item 6"));
        items.add(new Item("Item 7"));
        items.add(new Item("Item 8"));
        items.add(new Item("Item 9"));
        items.add(new Item("Item 10"));
        items.add(new Item("Item 11"));
        items.add(new Item("Item 12"));
        items.add(new Item("Item 13"));
        items.add(new Item("Item 14"));
        items.add(new Item("Item 15"));
        items.add(new Item("Item 16"));
        items.add(new Item("Item 17"));
        items.add(new Item("Item 18"));
        items.add(new Item("Item 19"));
        items.add(new Item("Item 20"));
        items.add(new Item("Item 21"));
        items.add(new Item("Item 22"));
        items.add(new Item("Item 23"));
        items.add(new Item("Item 24"));
        items.add(new Item("Item 25"));
        items.add(new Item("Item 26"));
        items.add(new Item("Item 27"));
        items.add(new Item("Item 28"));
        items.add(new Item("Item 29"));
        items.add(new Item("Item 30"));
    }

    /**
     * Returns the text color of the adapter's items.
     *
     * @return The text color of the adapter's items as an {@link Integer} value or -1, if no custom
     * color has been set
     */
    public final int getItemColor() {
        return itemColor;
    }

    /**
     * Sets the text color of the adapter's items.
     *
     * @param color
     *         The text color, which should be set, as an {@link Integer} value or -1, if no custom
     *         color should be set
     */
    public final void setItemColor(@ColorInt final int color) {
        this.itemColor = color;
    }

    /**
     * Returns the color of the adapter's dividers.
     *
     * @return The color of the adapter's dividers as an {@link Integer} value or -1, if no custom
     * color has been set
     */
    public final int getDividerColor() {
        return dividerColor;
    }

    /**
     * Sets the color of the adapter's dividers.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     */
    public final void setDividerColor(@ColorInt final int color) {
        this.dividerColor = color;
    }

    /**
     * Adds a new item to the adapter.
     *
     * @param item
     *         The item, which should be added, as an instance of the class {@link Item}. The item
     *         may not be null
     */
    public final void add(@NonNull final Item item) {
        ensureNotNull(item, "The item may not be null");
        items.add(item);
        notifyOnDataSetChanged();
    }

    /**
     * Adds a new divider to the adapter.
     *
     * @param divider
     *         The divider, which should be added, as an instance of the class {@link Divider}. The
     *         divider may not be null
     */
    public final void add(@NonNull final Divider divider) {
        ensureNotNull(divider, "The divider may not be null");
        items.add(divider);
        notifyOnDataSetChanged();
    }

    /**
     * Removes the item at a specific index.
     *
     * @param index
     *         The index of the item, which should be removed, as an {@link Integer} value
     */
    public final void remove(final int index) {
        items.remove(index);
        notifyOnDataSetChanged();
    }

    /**
     * Removes all items from the adapter.
     */
    public final void clear() {
        items.clear();
        notifyOnDataSetChanged();
    }

    /**
     * Sets, whether the <code>notifyDataSetChanged</code>-method should be called automatically,
     * when the adapter's items have been changed, or not.
     *
     * @param notifyOnChange
     *         True, if the <code>notifyDataSetChanged</code>-method should be called automatically,
     *         when the adapter's items have been changed, false otherwise
     */
    public final void notifyOnChange(final boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    @Override
    public final int getCount() {
        return items.size();
    }

    @Override
    public final Parcelable getItem(final int position) {
        return items.get(position);
    }

    @Override
    public final long getItemId(final int position) {
        return position;
    }

    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        int viewType = getItemViewType(position);
        Parcelable item = getItem(position);

        if (view == null) {
            if (viewType == PLACEHOLDER_VIEW_TYPE) {

            } else if (viewType == ITEM_VIEW_TYPE) {
                view = inflateItemView(parent);
            } else {
                view = inflateDividerView(parent);
            }
        }

        if (viewType == ITEM_VIEW_TYPE) {
            ItemViewHolder viewHolder = (ItemViewHolder) view.getTag();
            visualizeItem((Item) item, viewHolder);
        } else if (viewType == SEPARATOR_VIEW_TYPE) {
            DividerViewHolder viewHolder = (DividerViewHolder) view.getTag();
            visualizeDivider((Divider) item, viewHolder);
        }

        return view;
    }

    @Override
    public final int getViewTypeCount() {
        return 3;
    }

    @Override
    public final int getItemViewType(final int position) {
        Parcelable item = getItem(position);

        if (item == null) {
            return PLACEHOLDER_VIEW_TYPE;
        } else if (item instanceof Item) {
            return ITEM_VIEW_TYPE;
        } else {
            return SEPARATOR_VIEW_TYPE;
        }
    }

}