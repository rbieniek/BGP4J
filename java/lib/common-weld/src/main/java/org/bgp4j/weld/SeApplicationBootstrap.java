/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package org.bgp4j.weld;

import java.lang.annotation.Annotation;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class SeApplicationBootstrap {

	public static  void bootstrapApplication(String[] args, Annotation selection) {
		StartMain.PARAMETERS = args;
		
		Weld weld = new Weld();
		WeldContainer weldContainer = weld.initialize();
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(weld));
		weldContainer.event().select(SeApplicationStartEvent.class, selection).fire(new SeApplicationStartEvent());
	}

    static class ShutdownHook extends Thread {
        private final Weld weld;

        ShutdownHook(Weld weld) {
            this.weld = weld;
        }

        public void run() {
            weld.shutdown();
        }
    }
}
