# sdetAssessment
API Under Test: https://order-pizza-api.herokuapp.com/api/ui/

A REST-ful API for pizza ordering. Through this API, you can place order of a fake pizza to your endpoint. It includes the Curst, Flavor, order_id, size, table_no, and timestamp of the order. Create your OAuth token to start the order placement. You can fetch all orders using a GET request. And if an order is generated wrong, you can delete it at a DELETE request.

Used TestNG/RestAussred as my testing framework to test and validate the APIs belong to Pizza Ordering. Using TestNG to utilize the robust feature of TestNG’s annotations and flexibility to control tests to be ran. REST Assured is a Java library that provides a domain-specific language (DSL) for writing powerful, maintainable tests for RESTful APIs which provides easy accessibility to the response data.


Tests Performed:
1.	Get Orders
2.	Place an Order
3.	Place same Order to validate conflict
4.	Delete the order created due to completion of order or changes to order
5.	Delete non existing order
6.	Verify Auth features

Pros:
1.	Framework is fit for code-reusability
2.	Can be parameterized to run different set of data and multiple data
3.	We could integrate this test into CI/CD pipeline for stable deployments
4.	Fast and flexible

Cons:
1.	Not using POJO Classes for Order could cause data issue during request creation/response extract
2.	Difficult to implement Load Testing
3.	Requirements related to number of tables or flavor or size availability is not clear so unable to implement specific tests for those use cases

