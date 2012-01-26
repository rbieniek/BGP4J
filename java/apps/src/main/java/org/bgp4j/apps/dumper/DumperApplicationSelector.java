/**
 * 
 */
package org.bgp4j.apps.dumper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author rainer
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DumperApplicationSelector {

}
