package ru.techlab.risks.calculation.loans;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.model.AccountId;
import ru.techlab.risks.calculation.model.BaseCustomer;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.services.customer.CustomerService;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.ex.quality.QualityConvertionEx;
import ru.xegex.risks.libs.model.loan.LoanServCoeff;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RisksCalculationApplicationGoodTests {
	@Autowired
	private CassandraOperations cassandraOperations;

	@Autowired
	protected QualityService qualityService;
	@Autowired
	protected LoansService loansService;
	@Autowired
	protected DelayService delayService;
	@Autowired
	protected CustomerService customerService;

	private static boolean IS_RUN_ONCE = true;
	private static int RUN_ONCE_COUNTER_BEFORE;
	private static int RUN_ONCE_COUNTER_AFTER;



	/**
	 | branch | 5139 |
	 | loanAccountNumber | 045737 |
	 | loanAccountSuffix | 200 |
	 */
	//private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9499", "V79963", "200");
	//private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9499", "V79963", "200");
	private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("9472", "889684", "200");
	private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("9472", "889684", "200");

	private BaseLoan latestLoan = null;


	@Before
	public void setUp() throws DelayNotFoundException {
		if(IS_RUN_ONCE == true){
			if(RUN_ONCE_COUNTER_BEFORE == 0){
				RUN_ONCE_COUNTER_BEFORE++;
				cassandraOperations.execute("CREATE TABLE gfpf (GFCUS VARCHAR, GFCLC VARCHAR,GFCUN VARCHAR,GFCPNC VARCHAR,GFDAS VARCHAR,GFC1R VARCHAR,GFC2R VARCHAR,GFC3R VARCHAR,GFC4R VARCHAR,GFC5R VARCHAR,GFP1R VARCHAR,GFP2R VARCHAR,\n" +
						"GFP3R VARCHAR,GFP4R VARCHAR,GFP5R VARCHAR,GFCTP VARCHAR,GFCUB VARCHAR,GFCUC VARCHAR,GFCUD VARCHAR,GFCUZ VARCHAR,GFSAC VARCHAR,GFACO VARCHAR,GFCRF VARCHAR,GFLNM VARCHAR,GFCA2 VARCHAR,GFCNAP VARCHAR,\n" +
						"GFCNAR VARCHAR,GFCNAL VARCHAR,GFCOD DECIMAL,GFDCC DECIMAL,GFDLM DECIMAL,GFITRT DECIMAL,GFBRNM VARCHAR,GFCRB1 VARCHAR,GFCRB2 VARCHAR,GFADJ DECIMAL,GFERCP VARCHAR,GFERCC VARCHAR,GFDRC VARCHAR,\n" +
						"GFGRPS VARCHAR,GFCUNA VARCHAR,GFDASA VARCHAR,GFCUNM VARCHAR,GFCNAI VARCHAR,GFGRP VARCHAR,GFMTB VARCHAR,GFETX VARCHAR,GFYFON VARCHAR,GFDFRQ DECIMAL,GFFON VARCHAR,GFFOL VARCHAR,GFDEL VARCHAR,\n" +
						"PRIMARY KEY (GFCUS));\n" +
						"\n" +
						"CREATE TABLE sddu (DDCODE VARCHAR, DDABD VARCHAR, DDAND VARCHAR, DDASD VARCHAR, DDABC VARCHAR, DDASC VARCHAR, DDASV1 VARCHAR, DDASV2 VARCHAR, DDNCD VARCHAR, DDAMN DECIMAL, DDDT1 DECIMAL, DDDT2 DECIMAL, DDAP VARCHAR, \n" +
						"DDSP VARCHAR, DDOM VARCHAR, DDOI VARCHAR, DDCOM VARCHAR, DDCOMD1 DECIMAL, DDCOMD2 DECIMAL, DDACT VARCHAR, DDLAST VARCHAR, DDUID VARCHAR, DDWID VARCHAR, DDDT DECIMAL, DDTM DECIMAL, DDAFG VARCHAR, \n" +
						"DDCAT VARCHAR, DDAMR DECIMAL, DDRL VARCHAR, DDC3R VARCHAR, DDUNIQ VARCHAR, DDEP VARCHAR, DDNH VARCHAR, DDHNUM DECIMAL, DDSC0 VARCHAR, DDSC1 VARCHAR, DDSC2 VARCHAR, DDSC3 VARCHAR, DDSC4 VARCHAR, \n" +
						"DDSC5 VARCHAR, DDSC6 VARCHAR, DDSC7 VARCHAR, DDSC8 VARCHAR, DDSC9 VARCHAR, DDDT0 DECIMAL, DDFIL VARCHAR, DDCCY VARCHAR, DDC4R VARCHAR, DDANC VARCHAR, DDCOM0 VARCHAR, DDCOM1 VARCHAR, DDCOM2 VARCHAR, \n" +
						"DDFIND2 DECIMAL, DDFINM DECIMAL, DDFINA DECIMAL, DDPNT VARCHAR, DDEXP VARCHAR, DDSYS VARCHAR, DDDBTG VARCHAR, DDBUR VARCHAR, DDBUR2 VARCHAR, DDBURC VARCHAR, DDRST VARCHAR, DDRSTN DECIMAL, DDRVL VARCHAR, \n" +
						"DDLIMC VARCHAR, DDCOC VARCHAR, DDCOC2 VARCHAR, DDTCHM DECIMAL, DDTCHN DECIMAL, DDPRDR DECIMAL, DDINTD DECIMAL, DDBRRP VARCHAR, DDDRRP VARCHAR, DDRTMP DECIMAL, DDTRCP VARCHAR, DDRATP DECIMAL, DDGPRD DECIMAL, \n" +
						"DDGPRHN DECIMAL, DDINF0 VARCHAR, DDINF1 VARCHAR, DDINF2 VARCHAR, DDINF3 VARCHAR, DDINF4 VARCHAR, DDEVT0 VARCHAR, DDEVT1 VARCHAR, DDEVT2 VARCHAR, DDEVT3 VARCHAR, DDEVT4 VARCHAR, DDRUL0 VARCHAR, DDRUL1 VARCHAR, \n" +
						"DDRUL2 VARCHAR, DDRUL3 VARCHAR, DDRUL4 VARCHAR, DDBUF VARCHAR, DDIFIL VARCHAR, DDINDN DECIMAL, DDAPR VARCHAR, DDSPR DECIMAL, DDMSK DECIMAL, DDAGE DECIMAL, DDPRO DECIMAL, DDBUF2 VARCHAR, DDUCL VARCHAR, DDNDIR DECIMAL, \n" +
						"DDMAP DECIMAL, DDST VARCHAR, DDRES1 VARCHAR, DDRES2 VARCHAR, DDRES3 VARCHAR, DDRES4 VARCHAR, DDRES5 VARCHAR, DDRES6 VARCHAR, DDRES7 DECIMAL, DDRES8 DECIMAL, DDRES9 DECIMAL, DDRES10 DECIMAL, DDRES11 DECIMAL, DDRES12 DECIMAL, \n" +
						"DDBUF3 VARCHAR, \n" +
						"PRIMARY KEY (DDAND, DDABD, DDASD, DDACT, DDASV2, DDDT1, DDDT2));\n" +
						"\n" +
						"CREATE TABLE srru (RRABD VARCHAR, RRAND VARCHAR, RRASD VARCHAR, RRDT1 DECIMAL, RRIP VARCHAR, RRDTS DECIMAL, RRDTE DECIMAL, RRR1 VARCHAR, RRR2 VARCHAR, RRR3 DECIMAL, \n" +
						"PRIMARY KEY (RRAND, RRABD, RRASD, RRDT1, RRDTS, RRDTE, RRIP));");
			}
		}
	}

	/**
	 * Обслуживание долга по ссуде может быть признано хорошим, если:
	 * 1) платежи по основному долгу и процентам осуществляются своевременно и в полном объеме (нет просроченных платежей);
	 * 2) имеется случай (имеются случаи) просроченных платежей по основному долгу и (или) процентам в течение последних 180 календарных дней:
	 * 3) по ссудам, предоставленным юридическим лицам, - продолжительностью (общей продолжительностью) до 5 календарных дней включительно.
	 *
	 * Scenario: there are no delays for specified loan account
	 *
	 * Given: account number with no delays
	 	| branch | 5139 |
	 	| loanAccountNumber | 045737 |
    	| loanAccountSuffix | 200 |
    */
    @Test
    public void account_number_with_no_delays() throws LoanNotFoundException {
		latestLoan = loansService.getActiveAndNonPortfolioLoan(ACCOUNT_ID_WITH_NO_DELAY);
		Assert.assertNotEquals(null, latestLoan);
		/*
        try {
            List<BaseDelay> test = delayService.getDelaysByLoan(baseLoan).collect(Collectors.toList());
        } catch (DelayNotFoundException e) {
            e.printStackTrace();
        }*/
    }

	/**
	 * When: get no delays for specified account - the client receives loan quality GOOD
	 */
	@Test
	public void get_no_delays_for_specified_account(){
		LoanServCoeff loanQuality = qualityService.calculateLoanServCoeff(latestLoan);
		Assert.assertEquals(LoanServCoeff.GOOD, loanQuality);
	}

	/**
	 * When: only one delay for last 180 days - the client receives loan quality GOOD
	 */
	@Test
	public void get_one_delays_for_specified_account_and_() throws LoanNotFoundException, DelayNotFoundException {
		latestLoan = loansService.getActiveAndNonPortfolioLoan(ACCOUNT_ID_WITH_ONE_DELAY);
		LoanServCoeff loanQuality = qualityService.calculateLoanServCoeff(latestLoan);
		Assert.assertEquals(LoanServCoeff.GOOD, loanQuality);
	}

	@Test
	public void calculate_loan_quality_category() throws CustomerNotFoundEx, QualityConvertionEx {
		LoanServCoeff loanServCoeff = qualityService.calculateLoanServCoeff(latestLoan);
		BaseCustomer customer = customerService.getCustomer(latestLoan.getLoanAccountNumber());
		qualityService.calculateLoanQualityCategory(customer.getFinState(), loanServCoeff);
		Assert.assertEquals(LoanServCoeff.GOOD, loanServCoeff);
	}


	@After
	public void shutDown(){
		if(IS_RUN_ONCE == true){
			if(RUN_ONCE_COUNTER_AFTER == 0){
				RUN_ONCE_COUNTER_AFTER++;
				/*DropTableSpecification dropper = DropTableSpecification.dropTable("srrusddu");
				cassandraOperations.execute(dropper);*/
			}
		}
	}
}
