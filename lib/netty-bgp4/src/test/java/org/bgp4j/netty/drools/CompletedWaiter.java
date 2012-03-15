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
 * File: org.bgp4j.netty.drools.CompletedWaiter.java 
 */
package org.bgp4j.netty.drools;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CompletedWaiter {

	private Lock lock;
	private Condition waitFor;
	
	public CompletedWaiter() {
		lock = new ReentrantLock();
		waitFor = lock.newCondition();
	}
	
	public boolean waitForCompletion(int secondsToWait) throws InterruptedException {
		lock.lock();
		
		try {
			return waitFor.await(secondsToWait, TimeUnit.SECONDS);
		} finally {
			lock.unlock();
		}
	}
	
	public void complete() {
		lock.lock();
		
		try {
			waitFor.signalAll();
		} finally {
			lock.unlock();
		}
	}
}
