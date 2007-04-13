<%@ taglib uri="mega-action" prefix="a" %>

<a:messages all="true" filter="true"/>

<FORM ACTION="<a:action method="insert"/>" METHOD="POST">
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
</FORM>

<BR/>

<a href="<a:action action="/db/recordList"/>"><a:key key="add.label.back"/></a>
