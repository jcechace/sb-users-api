/**
 * Tests utilise Test Containers to run against a real database.
 * Although that would classify the tests as integration I personally
 * think that such tests are more likely to catch issue, and it also makes
 * the test more readable than using mocks. In practice a combination  with
 * unit tests would be used
 *
 * <br>
 *
 * Production grade coverage was not intended.
 * - Repositories should be tested (for the demo we assume that Spring Data JPA works as expected)
 * - More edge cases should be covered
 * - Security should be tested
 */
package net.cechacek.examples.users;