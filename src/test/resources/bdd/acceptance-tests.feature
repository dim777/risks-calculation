Feature: select active and non-portfolio loans
  Scenario: client call repository method to select specified loans s
    Given user pass datetime range for test (from: "2016-05-01" till: "2017-08-20")
      | content            |
      | I am making dinner |
      | I just woke up     |
      | I am going to work |
    When the client calls1 /version
    Then the client receives status code of 200
    And the client receives server version 1.0
  Scenario: change loan quality
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