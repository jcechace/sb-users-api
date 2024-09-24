/**
 * At the moment the DTO classes are almost identical to service-layer models.
 * However, this provides a clear separation of concerns and allows for future changes in the API
 * without affecting the service layer (e.g. the use of HTAOAS, different serialization, etc.)
 */
package net.cechacek.examples.users.api.dto;