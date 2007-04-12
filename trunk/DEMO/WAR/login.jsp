<%@ taglib uri="mega-action" prefix="a" %>

<a:messages all="true" filter="true"/>

<FORM ACTION="<a:action method="login"/>" METHOD="POST">
	<TABLE BORDER="0">
	<TR>
		<TD><a:key key="login.label.user"/></TD>
		<TD><INPUT TYPE="text" NAME="user" VALUE="<a:value property="user"/>"></TD>
	</TR>
	<TR>
		<TD COLSPAN="2"><INPUT TYPE="submit" VALUE="<a:key key="login.label.next"/>"></TD>
	</TR>
	</TABLE>
</FORM>
<BR>
<BR>
<BR>
<a href="<a:action action="/currentLocate"/>"><a:key key="list.label.locate"/></a>