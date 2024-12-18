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

import android.app.Activity;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

@Kroll.proxy(creatableInModule = DraggableModule.class)
public class ViewProxy extends TiViewProxy {

    public ViewProxy() {
        super();
    }

    @Override
    public void handleCreationDict(KrollDict options) {
        super.handleCreationDict(options);

        ConfigProxy config = new ConfigProxy(options.getKrollDict("draggableConfig"));

        this.setProperty("draggable", config);
    }

    @Override
    public TiUIView createView(Activity activity) {
        if (this.view == null) {
            TiUIView view = new DraggableImpl(this);

            setView(view);

            return view;
        }

        return this.view;
    }

    @Kroll.getProperty
    @Kroll.method
    public KrollProxy getDraggable() {
        return (KrollProxy) this.getProperty("draggable");
    }

}
