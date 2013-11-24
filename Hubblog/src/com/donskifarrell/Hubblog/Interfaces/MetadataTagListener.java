package com.donskifarrell.Hubblog.Interfaces;

import android.view.View;
import com.donskifarrell.Hubblog.Providers.Data.MetadataTag;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 21/11/13
 * Time: 19:05
 */
public interface MetadataTagListener {
    public void removeMetadataTag(MetadataTag tag, View view);
    public void addMetadataTag();
}
