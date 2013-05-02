/*
 Copyright 2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.service.component.fsimport;

//CHECKSTYLE:OFF
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import no.usit.fsws.studinfo.StudInfoService;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestClient {

  private static final String FS_STUDIEINFO = "<fs-studieinfo";
  private static ApplicationContext ctx;
  
  @BeforeClass
  public static void initContext() {
    try {
      ctx = new ClassPathXmlApplicationContext("fsimportContext.xml");
    } catch(Exception ex) {
      Assume.assumeNoException(ex);
    }
  }
  
  @Test
  public void testFsWsClient() {
    final StudInfoService studinfo = ctx.getBean("fsWsStudInfo", no.usit.fsws.studinfo.StudInfoService.class);
    assertThat(studinfo, is(notNullValue()));
    
    final String result = studinfo.getKursSI(217, -1, -1, -1, "B");
    assertThat(result, is(notNullValue()));
    assertThat(result.substring(0, FS_STUDIEINFO.length()), is(FS_STUDIEINFO));
  }

}
