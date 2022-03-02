/*
 *
 * Copyright 2022 The Johns Hopkins University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.dataconservancy.pass.indexer.checker.integration;

import org.dataconservancy.pass.client.PassClient;
import org.dataconservancy.pass.client.PassClientFactory;
import org.dataconservancy.pass.indexer.checker.app.IndexerCheckerApp;
import org.dataconservancy.pass.indexer.checker.app.PassCliException;
import org.dataconservancy.pass.model.User;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 *  Setup method to populate the index with ten users - necessary to match the non-empty test needed for production
 */
public class IndexerCheckerIT {
    static IndexerCheckerApp app = new IndexerCheckerApp(false);

    @Before
    public void populateIndex() throws InterruptedException {
        PassClient passClient = PassClientFactory.getPassClient();

        for( int i =1; i<=10; i++) {
            User testUser = new User();
            testUser.setFirstName("BeSsIe");
            testUser.setLastName("MoOcOw");
            String businessId = "infinityPlus" + i;
            List<String> locatorIds = new ArrayList<>();
            locatorIds.add(businessId);
            testUser.setLocatorIds(locatorIds);
            List<User.Role> roles = new ArrayList<>();
            roles.add(User.Role.SUBMITTER);
            testUser.setRoles(roles);
            URI userUri = passClient.createResource(testUser);
        }

        sleep(10000);// give indexer a chance to pick up these ussrs - we populate the index so that we are not searching against an empty index
    }

    /**
     *  The integration test
     */
    @Test
    public void CheckIndexerIT() {
        try {
            app.run();
        } catch (PassCliException e) {
            e.printStackTrace();
        }

    }
}
