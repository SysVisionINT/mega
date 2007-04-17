<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="mega-tags" prefix="t" %>

<a:messages all="true" filter="true"/>

<t:form method="insert">
	<TABLE BORDER="0">
	<TR>
		<TD><a:key key="add.label.id"/></TD>
		<TD><INPUT TYPE="text" NAME="id" VALUE="<a:value property="id"/>"></TD>
	</TR>
	<TR>
		<TD><a:key key="add.label.name"/></TD>
		<TD><INPUT TYPE="text" NAME="nome" VALUE="<a:value property="nome"/>"></TD>
	</TR>
	<TR>
		<TD COLSPAN="2"><INPUT TYPE="submit" VALUE="<a:key key="add.label.insert"/>"></TD>
	</TR>
	</TABLE>
</t:form>

<BR/>

<t:link action="/db/recordList"><a:key key="add.label.back"/></t:link>
