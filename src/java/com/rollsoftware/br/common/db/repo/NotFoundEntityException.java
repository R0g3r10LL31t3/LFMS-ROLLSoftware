/*
 *          Copyright 2016-2026 Rogério Lecarião Leite
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
 *  CEO 2016: Rogério Lecarião Leite; ROLL Software
 */
package com.rollsoftware.br.common.db.repo;

import java.sql.SQLException;

/**
 *
 * @author Rogério
 * @date November, 2016
 */
public class NotFoundEntityException extends SQLException {

    public NotFoundEntityException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public NotFoundEntityException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public NotFoundEntityException(String reason) {
        super(reason);
    }

    public NotFoundEntityException() {
    }

    public NotFoundEntityException(Throwable cause) {
        super(cause);
    }

    public NotFoundEntityException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public NotFoundEntityException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public NotFoundEntityException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }

}
