// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract TxLedger {
    struct Transaction {
        uint256 id;
        string accountId;
        string targetAccountId;
        uint256 amount;
        string currency;
        uint256 timestamp;
    }

    Transaction[] public transactions;
    uint256 public nextTransactionId;

    event TransactionRecorded(
        uint256 indexed id,
        string accountId,
        string targetAccountId,
        uint256 amount,
        string currency,
        uint256 timestamp
    );

    /**
     * @notice Registra una nueva transacción bancaria.
     * @param accountId Identificador de la cuenta origen.
     * @param targetAccountId Identificador de la cuenta destino (puede ser vacío para depósitos/retiros).
     * @param amount Monto de la transacción.
     * @param currency Moneda utilizada.
     */
    function recordTransaction(
        string calldata accountId,
        string calldata targetAccountId,
        uint256 amount,
        string calldata currency
    ) external {
        uint256 id = nextTransactionId++;
        uint256 timestamp = block.timestamp;
        transactions.push(
            Transaction(
                id,
                accountId,
                targetAccountId,
                amount,
                currency,
                timestamp
            )
        );
        emit TransactionRecorded(
            id,
            accountId,
            targetAccountId,
            amount,
            currency,
            timestamp
        );
    }

    /**
     * @notice Recupera una transacción por su ID.
     * @param id Identificador de la transacción.
     * @return La transacción registrada.
     */
    function getTransaction(
        uint256 id
    ) external view returns (Transaction memory) {
        require(id < transactions.length, "Transaction does not exist");
        return transactions[id];
    }

    /**
     * @notice Retorna el número total de transacciones registradas.
     * @return Cantidad de transacciones.
     */
    function getTransactionCount() external view returns (uint256) {
        return transactions.length;
    }
}
