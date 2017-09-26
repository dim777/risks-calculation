#Обслуживание долга по ссуде может быть признано хорошим, если
Feature: all payments on main loan and percents account processed fine (there are no delays in loans), mark as GOOD

  #платежи по основному долгу и процентам осуществляются своевременно и в полном объеме (нет просроченных платежей);
  Scenario: there are no delays for specified loan account
    Given input number account for request
      |5139|045737|200
    When the client calls1 /version
    Then the client receives status code of 200
    And the client receives server version 1.0

  #имеется случай (имеются случаи) просроченных платежей по основному долгу и (или) процентам в течение последних 180 календарных дней
  Scenario: there are 1 delay for specified loan account
    Given input number account for request
      |9796|633317|700
    When the client calls1 /version
    Then the client receives status code of 200
    And the client receives server version 1.0

  #по ссудам, предоставленным юридическим лицам, - продолжительностью (общей продолжительностью) до 5 календарных дней включительно.
  Scenario: for company loan delay for max 5 days
    Given input number account for request
      |9796|633317|700
    When the client calls1 /version
    Then the client receives status code of 200
    And the client receives server version 1.0

  Scenario: there are delays for specified loan account
    Given send messqge with modified ddc3r in sddu
      | content            |
      | I am making dinner |
      | I just woke up     |
      | I am going to work |
    When the client calls1 /version
    Then the client receives status code of 200
    And the client receives server version 1.0
  Scenario: save modified data to BIS
    Given receive message with
      | content            |
      | I am making dinner |
      | I just woke up     |
      | I am going to work |
    When the client calls1 /version
    Then the client receives status code of 200
    And the client receives server version 1.0