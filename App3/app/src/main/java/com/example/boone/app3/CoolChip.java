package com.example.boone.app3;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.Chip;

import java.util.UUID;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link Chip} that's used when the user creates a custom chip.
 *
 * A custom chip can be created whenever the user inputs text into the chip
 * input layout, that doesn't match filterable information, and they press
 * enter on the software keyboard.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
final class CoolChip extends Chip {
    private String title;


    public  CoolChip(String title) {
        this.title = title;
        setFilterable(false);
    }


    @Nullable
    @Override
    public Object getId() {
        return null;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return null;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

}
