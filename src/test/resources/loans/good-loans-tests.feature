#Обслуживание долга по ссуде может быть признано хорошим, если
Feature: all payments on main loan and percents account processed fine (there are no delays in loans), mark as GOOD

  #платежи по основному долгу и процентам осуществляются своевременно и в полном объеме (нет просроченных платежей);
  Scenario: there are no delays for specified loan account
    Given account number with no delays
      | branch | 5139 |
      | loanAccountNumber | 045737 |
      | loanAccountSuffix | 200 |
    When get no delays for specified account
    Then the client receives loan quality GOOD