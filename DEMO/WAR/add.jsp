<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="mega-tags" prefix="t" %>

<a:messages all="true" filter="true"/>

<t:form method="insert">
	<TABLE BORDER="0">
	<TR>
		<TD><a:key key="add.label.id"/></TD>
		<TD><t:inputText property="id" size="5" tabIndex="1"/></TD>
	</TR>
	<TR>
		<TD><a:key key="add.label.name"/></TD>
		<TD><t:inputText property="nome" size="50" tabIndex="2"/></TD>
	</TR>
	<TR>
		<TD><a:key key="add.label.admin"/></TD>
		<TD><t:checkBox property="admin" tabIndex="3"/></TD>
	</TR>	
		<TR>
		<TD><a:key key="add.label.xpto"/></TD>
		<TD>1 <t:multiCheck property="xpto" value="1" tabIndex="4"/><br/>
		2 <t:multiCheck property="xpto" value="2" tabIndex="5"/><br/>
		3 <t:multiCheck property="xpto" value="3" tabIndex="6"/><br/>
		4 <t:multiCheck property="xpto" value="4" tabIndex="7"/><br/>
		5 <t:multiCheck property="xpto" value="5" tabIndex="8"/></TD>
	</TR>	
	<TR>
		<TD COLSPAN="2"><INPUT TYPE="submit" VALUE="<a:key key="add.label.insert"/>"></TD>
	</TR>
	</TABLE>
</t:form>

<BR/>

<t:link action="/db/recordList"><a:key key="add.label.back"/></t:link>
