/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.nalukit.gwtbootstarternalu.client;

import com.github.nalukit.gwtbootstarternalu.shared.model.ControllerData;
import com.github.nalukit.gwtbootstarternalu.shared.model.DataConstants;
import com.github.nalukit.gwtbootstarternalu.shared.model.NaluGeneraterParms;
import com.github.nalukit.gwtbootstarternalu.shared.model.WidgetLibrary;
import com.github.nalukit.nalu.client.application.IsContext;

public class ApplicationContext
  implements IsContext {

  private String version;

  private NaluGeneraterParms naluGeneraterParms;

  public ApplicationContext() {
    this.version = Version.VERSION;

    this.naluGeneraterParms = new NaluGeneraterParms();
    this.naluGeneraterParms.setGroupId("com.example");
    this.naluGeneraterParms.setArtefactId("MyTestProject");
    this.naluGeneraterParms.setApplicationLoader(true);
    this.naluGeneraterParms.setDebug(true);
    this.naluGeneraterParms.setGwtVersion(DataConstants.GWT_VERSION_2_8_2);
    this.naluGeneraterParms.setWidgetLibrary(WidgetLibrary.DOMINO_UI);
    this.naluGeneraterParms.getControllers()
                           .add(new ControllerData("Screen01",
                                                   "screen01",
                                                   true,
                                                   false,
                                                   true,
                                                   true));
    this.naluGeneraterParms.getControllers()
                           .add(new ControllerData("Screen02",
                                                   "screen02",
                                                   false,
                                                   false,
                                                   true,
                                                   true));
    this.naluGeneraterParms.getControllers()
                           .add(new ControllerData("Screen03",
                                                   "screen03",
                                                   false,
                                                   true,
                                                   true,
                                                   true));
    this.naluGeneraterParms.getControllers()
                           .add(new ControllerData("Screen04",
                                                   "screen04",
                                                   false,
                                                   false,
                                                   true,
                                                   true));
    this.naluGeneraterParms.getControllers()
                           .add(new ControllerData("Screen05",
                                                   "screen05",
                                                   false,
                                                   false,
                                                   true,
                                                   true));
  }

  public String getVersion() {
    return version;
  }

  public NaluGeneraterParms getNaluGeneraterParms() {
    return naluGeneraterParms;
  }

  public void setNaluGeneraterParms(NaluGeneraterParms naluGeneraterParms) {
    this.naluGeneraterParms = naluGeneraterParms;
  }
}
