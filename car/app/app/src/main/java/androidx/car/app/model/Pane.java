/*
 * Copyright 2020 The Android Open Source Project
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

package androidx.car.app.model;

import static java.util.Objects.requireNonNull;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.car.app.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list of rows used for displaying informational content and a set of {@link Action}s
 * that users can perform based on such content.
 */
public final class Pane {
    @Keep
    private final List<Action> mActionList;
    @Keep
    private final List<Row> mRows;
    @Keep
    private final boolean mIsLoading;

    /** Constructs a new builder of {@link Pane}. */
    // TODO(b/175827428): remove once host is changed to use new public ctor.
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the list of {@link Action}s displayed alongside the {@link Row}s in this pane.
     *
     * @deprecated use {@link #getActionList()} instead.
     */
    // TODO(jayyoo): remove once {@link #getActionList()} is used in the host.
    @Deprecated
    @Nullable
    public ActionList getActions() {
        return mActionList.isEmpty() ? null : ActionList.create(mActionList);
    }

    /**
     * Returns the list of {@link Action}s displayed alongside the {@link Row}s in this pane.
     */
    @NonNull
    public List<Action> getActionList() {
        return mActionList;
    }

    /**
     * Returns the list of {@link Row} objects that make up the {@link Pane}.
     *
     * @deprecated use {@link #getRowList()} ()} instead.
     */
    // TODO(b/177591128): remove after host(s) no longer reference this.
    @SuppressWarnings("unchecked")
    @Deprecated
    @NonNull
    public List<Object> getRows() {
        return (List<Object>) (List<?>) mRows;
    }

    /**
     * Returns the list of {@link Row} objects that make up the {@link Pane}.
     */
    // TODO(b/177591128): rename back to getRows after removal of the deprecated API.
    @NonNull
    public List<Row> getRowList() {
        return mRows;
    }

    /**
     * Returns the {@code true} if the {@link Pane} is loading.*
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    @NonNull
    public String toString() {
        return "[ rows: "
                + (mRows != null ? mRows.toString() : null)
                + ", action list: "
                + mActionList
                + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(mRows, mActionList, mIsLoading);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Pane)) {
            return false;
        }
        Pane otherPane = (Pane) other;

        return mIsLoading == otherPane.mIsLoading
                && Objects.equals(mActionList, otherPane.mActionList)
                && Objects.equals(mRows, otherPane.mRows);
    }

    Pane(Builder builder) {
        mRows = CollectionUtils.unmodifiableCopy(builder.mRows);
        mActionList = CollectionUtils.unmodifiableCopy(builder.mActionList);
        mIsLoading = builder.mIsLoading;
    }

    /** Constructs an empty instance, used by serialization code. */
    private Pane() {
        mRows = Collections.emptyList();
        mActionList = Collections.emptyList();
        mIsLoading = false;
    }

    /** A builder of {@link Pane}. */
    public static final class Builder {
        final List<Row> mRows = new ArrayList<>();
        List<Action> mActionList = new ArrayList<>();
        boolean mIsLoading;

        /**
         * Sets whether the {@link Pane} is in a loading state.
         *
         * <p>If set to {@code true}, the UI will display a loading indicator where the list content
         * would be otherwise. The caller is expected to call {@link
         * androidx.car.app.Screen#invalidate()} and send the new template content
         * to the host once the data is ready. If set to {@code false}, the UI shows the actual row
         * contents.
         *
         * @see #build
         */
        @NonNull
        public Builder setLoading(boolean isLoading) {
            this.mIsLoading = isLoading;
            return this;
        }

        /**
         * Adds a row to display in the list.
         *
         * @throws NullPointerException if {@code row} is {@code null}.
         */
        @NonNull
        public Builder addRow(@NonNull Row row) {
            mRows.add(requireNonNull(row));
            return this;
        }

        /**
         * Sets multiple {@link Action}s to display alongside the rows in the pane.
         *
         * <p>By default, no actions are displayed.
         *
         * @throws NullPointerException if {@code actions} is {@code null}.
         * @deprecated use {@link #setActionList(List)} instead.
         */
        // TODO(jayyoo): remove once {@link #setActionList(List)} is used in the host.
        @Deprecated
        @NonNull
        public Builder setActions(@NonNull List<Action> actions) {
            return setActionList(actions);
        }

        /**
         * Sets multiple {@link Action}s to display alongside the rows in the pane.
         *
         * <p>By default, no actions are displayed.
         *
         * @throws NullPointerException if {@code actions} is {@code null}.
         */
        @NonNull
        public Builder setActionList(@NonNull List<Action> actions) {
            requireNonNull(actions);
            for (Action action : actions) {
                if (action == null) {
                    throw new IllegalArgumentException(
                            "Disallowed null action found in action list");
                }
                mActionList.add(action);
            }
            return this;
        }

        /**
         * Constructs the row list defined by this builder.
         *
         * @throws IllegalStateException if the pane is in loading state and also contains rows, or
         *                               vice-versa.
         */
        @NonNull
        public Pane build() {
            int size = size();
            if (size > 0 == mIsLoading) {
                throw new IllegalStateException(
                        "The pane is set to loading but is not empty, or vice versa");
            }

            return new Pane(this);
        }

        private int size() {
            return mRows.size();
        }

        /** Returns an empty {@link Builder} instance. */
        public Builder() {
        }
    }
}
