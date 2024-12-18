/**
 * An enhanced fork of the original TiDraggable module by Pedro Enrique,
 * allows for simple creation of "draggable" views.
 * <p>
 * Copyright (C) 2013 Seth Benjamin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * -- Original License --
 * <p>
 * Copyright 2012 Pedro Enrique
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ti.draggable;

import android.view.View;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollPropertyChange;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollProxyListener;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.util.TiConvert;

import java.lang.ref.WeakReference;
import java.util.List;

@Kroll.proxy
public class ConfigProxy extends KrollProxy implements KrollProxyListener {
    public int threshold = 0;
    protected WeakReference<DraggableImpl> draggableImpl;

    public ConfigProxy(KrollDict config) {
        super();

        properties.put("enabled", config == null || !config.containsKeyAndNotNull("enabled") || TiConvert.toBoolean(config, "enabled", true));
        properties.put("ensureRight", config != null && config.containsKeyAndNotNull("ensureRight") && TiConvert.toBoolean(config, "ensureRight", false));
        properties.put("ensureBottom", config != null && config.containsKeyAndNotNull("ensureBottom") && TiConvert.toBoolean(config, "ensureBottom", false));
        properties.put("axis", config != null && config.containsKeyAndNotNull("axis") ? TiConvert.toString(config, "axis") : null);
        properties.put("maps", config != null && config.containsKeyAndNotNull("maps") ? config.get("maps") : null);
        properties.put("minLeft", config != null && config.containsKeyAndNotNull("minLeft") ? TiConvert.toTiDimension(config, "minLeft", TiDimension.TYPE_LEFT) : null);
        properties.put("maxLeft", config != null && config.containsKeyAndNotNull("maxLeft") ? TiConvert.toTiDimension(config, "maxLeft", TiDimension.TYPE_LEFT) : null);
        properties.put("minTop", config != null && config.containsKeyAndNotNull("minTop") ? TiConvert.toTiDimension(config, "minTop", TiDimension.TYPE_TOP) : null);
        properties.put("maxTop", config != null && config.containsKeyAndNotNull("maxTop") ? TiConvert.toTiDimension(config, "maxTop", TiDimension.TYPE_TOP) : null);
        properties.put("threshold", config != null && config.containsKeyAndNotNull("threshold") ? TiConvert.toInt(config, "threshold") : null);

        setModelListener(this);
    }

    public Integer getDimensionAsPixels(String key) {
        KrollDict props = this.getProperties();

        if (props.containsKeyAndNotNull(key) && props.get(key) instanceof TiDimension dimension) {
            View decorView = this.getDecorView();

            return dimension.getAsPixels(decorView);
        }

        return null;
    }

    public DraggableImpl getDraggableImpl() {
        return draggableImpl != null ? draggableImpl.get() : null;
    }

    public void setDraggableImpl(WeakReference<DraggableImpl> draggableView) {
        draggableImpl = draggableView;
    }

    @Kroll.method
    public void setConfig(Object[] args) {
        if (args.length >= 2) {
            setPropertyAndFire((String) args[0], args[1]);
        } else if (args.length == 1) {
            applyProperties(args[0]);
        }
    }

    @Kroll.method
    @Kroll.getProperty
    public int getDefaultThreshold() {
        return this.threshold;
    }

    @Override
    public void listenerAdded(String type, int count, KrollProxy proxy) {
        // Unused
    }

    @Override
    public void listenerRemoved(String type, int count, KrollProxy proxy) {
        // Unused
    }

    @Override
    public void processProperties(KrollDict properties) {
        // Unused
    }

    @Override
    public void propertiesChanged(List<KrollPropertyChange> changes, KrollProxy proxy) {
        // Unused
    }

    @Override
    public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy) {
        if (key.equals("maps")) {
            DraggableImpl impl = getDraggableImpl();

            if (impl != null) {
                getDraggableImpl().listener.prepareMappedProxies();
            }
        } else if (key.equals("minLeft") || key.equals("maxLeft")) {
            properties.put(key, TiConvert.toTiDimension(TiConvert.toString(newValue), TiDimension.TYPE_LEFT));
        } else if (key.equals("minTop") || key.equals("maxTop")) {
            properties.put(key, TiConvert.toTiDimension(TiConvert.toString(newValue), TiDimension.TYPE_TOP));
        }
    }

    public View getDecorView() {
        return TiApplication.getAppCurrentActivity().getWindow().getDecorView();
    }
}
