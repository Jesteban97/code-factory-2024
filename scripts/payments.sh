curl -H "Content-Type: application/json" -d '{ "number": "3514019106542676", "expirationYear": 2022, "expirationMonth": 3, "holderName": "ARTURO F KENNEDY T", "cvc": 333, "balance": 10000000 }' localhost:8080/api/credit-cards
curl -H "Content-Type: application/json" localhost:8080/api/credit-cards
curl -H "Content-Type: application/json" -d '{ "reservationId": 987, "cardNumber": "3514019106542676", "cardCvc": 333, "cardHolderName": "ARTURO F KENNEDY T" }' localhost:8080/api/payments
