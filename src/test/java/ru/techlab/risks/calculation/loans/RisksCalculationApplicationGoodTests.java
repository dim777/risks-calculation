package ru.techlab.risks.calculation.loans;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cassandra.core.keyspace.DropTableSpecification;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.techlab.risks.calculation.model.AccountId;
import ru.techlab.risks.calculation.model.BaseLoan;
import ru.techlab.risks.calculation.services.delay.DelayService;
import ru.techlab.risks.calculation.services.loans.LoansService;
import ru.techlab.risks.calculation.services.quality.QualityService;
import ru.xegex.risks.libs.ex.delays.DelayNotFoundException;
import ru.xegex.risks.libs.ex.loans.LoanNotFoundException;
import ru.xegex.risks.libs.model.loan.LoanQuality;

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

	private static boolean IS_RUN_ONCE = true;
	private static int RUN_ONCE_COUNTER_BEFORE;
	private static int RUN_ONCE_COUNTER_AFTER;



	/**
	 | branch | 5139 |
	 | loanAccountNumber | 045737 |
	 | loanAccountSuffix | 200 |
	 */
	private static final AccountId ACCOUNT_ID_WITH_NO_DELAY = new AccountId("5139", "045737", "200");
	private static final AccountId ACCOUNT_ID_WITH_ONE_DELAY = new AccountId("5139", "045737", "200");

	private BaseLoan latestLoan = null;


	@Before
	public void setUp() throws DelayNotFoundException {
		if(IS_RUN_ONCE == true){
			if(RUN_ONCE_COUNTER_BEFORE == 0){
				RUN_ONCE_COUNTER_BEFORE++;
				cassandraOperations.execute("DROP TABLE IF EXISTS srrusddu");
				cassandraOperations.execute("DROP TABLE IF EXISTS sddu");

				cassandraOperations.execute("CREATE TABLE sddu (DDCODE VARCHAR, DDABD VARCHAR, DDAND VARCHAR, DDASD VARCHAR, DDABC VARCHAR, DDASC VARCHAR, DDASV1 VARCHAR, DDASV2 VARCHAR, DDNCD VARCHAR, DDAMN DECIMAL, DDDT1 DECIMAL, DDDT2 DECIMAL, DDAP VARCHAR,\n" +
						"DDSP VARCHAR, DDOM VARCHAR, DDOI VARCHAR, DDCOM VARCHAR, DDCOMD1 DECIMAL, DDCOMD2 DECIMAL, DDACT VARCHAR, DDLAST VARCHAR, DDUID VARCHAR, DDWID VARCHAR, DDDT DECIMAL, DDTM DECIMAL, DDAFG VARCHAR,\n" +
						"DDCAT VARCHAR, DDAMR DECIMAL, DDRL VARCHAR, DDC3R VARCHAR, DDUNIQ VARCHAR, DDEP VARCHAR, DDNH VARCHAR, DDHNUM DECIMAL, DDSC0 VARCHAR, DDSC1 VARCHAR, DDSC2 VARCHAR, DDSC3 VARCHAR, DDSC4 VARCHAR,\n" +
						"DDSC5 VARCHAR, DDSC6 VARCHAR, DDSC7 VARCHAR, DDSC8 VARCHAR, DDSC9 VARCHAR, DDDT0 DECIMAL, DDFIL VARCHAR, DDCCY VARCHAR, DDC4R VARCHAR, DDANC VARCHAR, DDCOM0 VARCHAR, DDCOM1 VARCHAR, DDCOM2 VARCHAR,\n" +
						"DDFIND2 DECIMAL, DDFINM DECIMAL, DDFINA DECIMAL, DDPNT VARCHAR, DDEXP VARCHAR, DDSYS VARCHAR, DDDBTG VARCHAR, DDBUR VARCHAR, DDBUR2 VARCHAR, DDBURC VARCHAR, DDRST VARCHAR, DDRSTN DECIMAL, DDRVL VARCHAR,\n" +
						"DDLIMC VARCHAR, DDCOC VARCHAR, DDCOC2 VARCHAR, DDTCHM DECIMAL, DDTCHN DECIMAL, DDPRDR DECIMAL, DDINTD DECIMAL, DDBRRP VARCHAR, DDDRRP VARCHAR, DDRTMP DECIMAL, DDTRCP VARCHAR, DDRATP DECIMAL, DDGPRD DECIMAL,\n" +
						"DDGPRHN DECIMAL, DDINF0 VARCHAR, DDINF1 VARCHAR, DDINF2 VARCHAR, DDINF3 VARCHAR, DDINF4 VARCHAR, DDEVT0 VARCHAR, DDEVT1 VARCHAR, DDEVT2 VARCHAR, DDEVT3 VARCHAR, DDEVT4 VARCHAR, DDRUL0 VARCHAR, DDRUL1 VARCHAR,\n" +
						"DDRUL2 VARCHAR, DDRUL3 VARCHAR, DDRUL4 VARCHAR, DDBUF VARCHAR, DDIFIL VARCHAR, DDINDN DECIMAL, DDAPR VARCHAR, DDSPR DECIMAL, DDMSK DECIMAL, DDAGE DECIMAL, DDPRO DECIMAL, DDBUF2 VARCHAR, DDUCL VARCHAR, DDNDIR DECIMAL,\n" +
						"DDMAP DECIMAL, DDST VARCHAR, DDRES1 VARCHAR, DDRES2 VARCHAR, DDRES3 VARCHAR, DDRES4 VARCHAR, DDRES5 VARCHAR, DDRES6 VARCHAR, DDRES7 DECIMAL, DDRES8 DECIMAL, DDRES9 DECIMAL, DDRES10 DECIMAL, DDRES11 DECIMAL, DDRES12 DECIMAL, DDBUF3 VARCHAR,\n" +
						"PRIMARY KEY (DDACT, DDASV2, DDABD, DDAND, DDASD));");
				cassandraOperations.execute("INSERT INTO sddu (DDCODE, DDABD, DDAND, DDASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ');");

				cassandraOperations.execute(
						"CREATE TABLE srrusddu (DDCODE VARCHAR, SABD VARCHAR, SAND VARCHAR, SASD VARCHAR, DDABC VARCHAR, DDASC VARCHAR, DDASV1 VARCHAR, DDASV2 VARCHAR, DDNCD VARCHAR, DDAMN DECIMAL, DDDT1 DECIMAL, DDDT2 DECIMAL, DDAP VARCHAR, \n" +
						"DDSP VARCHAR, DDOM VARCHAR, DDOI VARCHAR, DDCOM VARCHAR, DDCOMD1 DECIMAL, DDCOMD2 DECIMAL, DDACT VARCHAR, DDLAST VARCHAR, DDUID VARCHAR, DDWID VARCHAR, DDDT DECIMAL, DDTM DECIMAL, DDAFG VARCHAR, \n" +
						"DDCAT VARCHAR, DDAMR DECIMAL, DDRL VARCHAR, DDC3R VARCHAR, DDUNIQ VARCHAR, DDEP VARCHAR, DDNH VARCHAR, DDHNUM DECIMAL, DDSC0 VARCHAR, DDSC1 VARCHAR, DDSC2 VARCHAR, DDSC3 VARCHAR, DDSC4 VARCHAR, \n" +
						"DDSC5 VARCHAR, DDSC6 VARCHAR, DDSC7 VARCHAR, DDSC8 VARCHAR, DDSC9 VARCHAR, DDDT0 DECIMAL, DDFIL VARCHAR, DDCCY VARCHAR, DDC4R VARCHAR, DDANC VARCHAR, DDCOM0 VARCHAR, DDCOM1 VARCHAR, DDCOM2 VARCHAR, \n" +
						"DDFIND2 DECIMAL, DDFINM DECIMAL, DDFINA DECIMAL, DDPNT VARCHAR, DDEXP VARCHAR, DDSYS VARCHAR, DDDBTG VARCHAR, DDBUR VARCHAR, DDBUR2 VARCHAR, DDBURC VARCHAR, DDRST VARCHAR, DDRSTN DECIMAL, DDRVL VARCHAR, \n" +
						"DDLIMC VARCHAR, DDCOC VARCHAR, DDCOC2 VARCHAR, DDTCHM DECIMAL, DDTCHN DECIMAL, DDPRDR DECIMAL, DDINTD DECIMAL, DDBRRP VARCHAR, DDDRRP VARCHAR, DDRTMP DECIMAL, DDTRCP VARCHAR, DDRATP DECIMAL, DDGPRD DECIMAL, \n" +
						"DDGPRHN DECIMAL, DDINF0 VARCHAR, DDINF1 VARCHAR, DDINF2 VARCHAR, DDINF3 VARCHAR, DDINF4 VARCHAR, DDEVT0 VARCHAR, DDEVT1 VARCHAR, DDEVT2 VARCHAR, DDEVT3 VARCHAR, DDEVT4 VARCHAR, DDRUL0 VARCHAR, DDRUL1 VARCHAR, \n" +
						"DDRUL2 VARCHAR, DDRUL3 VARCHAR, DDRUL4 VARCHAR, DDBUF VARCHAR, DDIFIL VARCHAR, DDINDN DECIMAL, DDAPR VARCHAR, DDSPR DECIMAL, DDMSK DECIMAL, DDAGE DECIMAL, DDPRO DECIMAL, DDBUF2 VARCHAR, DDUCL VARCHAR, DDNDIR DECIMAL, \n" +
						"DDMAP DECIMAL, DDST VARCHAR, DDRES1 VARCHAR, DDRES2 VARCHAR, DDRES3 VARCHAR, DDRES4 VARCHAR, DDRES5 VARCHAR, DDRES6 VARCHAR, DDRES7 DECIMAL, DDRES8 DECIMAL, DDRES9 DECIMAL, DDRES10 DECIMAL, DDRES11 DECIMAL, DDRES12 DECIMAL, \n" +
						"DDBUF3 VARCHAR, RRDT1 DECIMAL, RRIP VARCHAR, RRDTS DECIMAL, RRDTE DECIMAL, RRR1 VARCHAR, RRR2 VARCHAR, RRR3 DECIMAL, PRIMARY KEY (SABD, SAND, SASD, RRDT1, RRDTS, RRDTE, RRIP));");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'P',1140722,1140723,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'P',1141222,1141223,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1150122,1150217,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'P',1150122,1150219,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1150223,1150313,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'P',1150223,9999999,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1150323,1170207,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1170226,1170228,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1170326,1170328,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1170426,1170705,' ','   ',0);");
				cassandraOperations.execute("INSERT INTO srrusddu (DDCODE, SABD, SAND, SASD, DDABC, DDASC, DDASV1, DDASV2, DDNCD, DDAMN, DDDT1, DDDT2, DDAP, DDSP, DDOM, DDOI, DDCOM, DDCOMD1, DDCOMD2, DDACT, DDLAST, DDUID, DDWID, DDDT, DDTM, DDAFG, DDCAT, DDAMR, DDRL, DDC3R, DDUNIQ, DDEP, DDNH, DDHNUM, DDSC0, DDSC1, DDSC2, DDSC3, DDSC4, DDSC5, DDSC6, DDSC7, DDSC8, DDSC9, DDDT0, DDFIL, DDCCY, DDC4R, DDANC, DDCOM0, DDCOM1, DDCOM2, DDFIND2, DDFINM, DDFINA, DDPNT, DDEXP, DDSYS, DDDBTG, DDBUR, DDBUR2, DDBURC, DDRST, DDRSTN, DDRVL, DDLIMC, DDCOC, DDCOC2, DDTCHM, DDTCHN, DDPRDR, DDINTD, DDBRRP, DDDRRP, DDRTMP, DDTRCP, DDRATP, DDGPRD, DDGPRHN, DDINF0, DDINF1, DDINF2, DDINF3, DDINF4, DDEVT0, DDEVT1, DDEVT2, DDEVT3, DDEVT4, DDRUL0, DDRUL1, DDRUL2, DDRUL3, DDRUL4, DDBUF, DDIFIL, DDINDN, DDAPR, DDSPR, DDMSK, DDAGE, DDPRO, DDBUF2, DDUCL, DDNDIR, DDMAP, DDST, DDRES1, DDRES2, DDRES3, DDRES4, DDRES5, DDRES6, DDRES7, DDRES8, DDRES9, DDRES10, DDRES11, DDRES12, DDBUF3, RRDT1, RRIP, RRDTS, RRDTE, RRR1, RRR2, RRR3) VALUES('SLD','9472','889684','200','9472','001','   ','000','PRP-R70-JKMH-0016   ',-300000000,1140422,1180926,'Y','Y','Y','Y','   ',0,1140422,'Y','Y','X1MKSTL   ','SN1GJSS1  ',1170209,94212,'8','C',0,'Y','05','N','N','N',0,' ',' ',' ',' ',' ',' ',' ',' ','N',' ',0,'R70','RUR','77','      ','   ','   ','   ',0,0,0,'    ','JKM2',' ','        ',' ','Y','889684         ',' ',0,' ','  ','  ','    ',0,0,0,0,'03','10',0,'    ',0,0,0,' ',' ',' ',' ',' ',' ','N',' ','4','2','NNN','   ','   ','   ','   ','             ','R70',134890,'N',0,0,0,0,'05        ',' ',0,0,' ',' ',' ','  ','  ','   ','   ',0,0,1180926,0,0,0,'                                                                                                                                ',1140422,'I',1170726,9999999,' ','   ',0);");
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
		LoanQuality loanQuality = qualityService.calculateLoanQuality(latestLoan);
		Assert.assertEquals(LoanQuality.GOOD, loanQuality);
	}

	/**
	 * When: only one delay for last 180 days - the client receives loan quality GOOD
	 */
	@Test
	public void get_one_delays_for_specified_account_and_() throws LoanNotFoundException, DelayNotFoundException {
		latestLoan = loansService.getActiveAndNonPortfolioLoan(ACCOUNT_ID_WITH_ONE_DELAY);
		LoanQuality loanQuality = qualityService.calculateLoanQuality(latestLoan);
		Assert.assertEquals(LoanQuality.GOOD, loanQuality);
	}

	@After
	public void shutDown(){
		if(IS_RUN_ONCE == true){
			if(RUN_ONCE_COUNTER_AFTER == 0){
				RUN_ONCE_COUNTER_AFTER++;
				DropTableSpecification dropper = DropTableSpecification.dropTable("srrusddu");
				cassandraOperations.execute(dropper);
			}
		}
	}
}
