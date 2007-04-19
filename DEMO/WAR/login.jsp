<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="mega-tags" prefix="t" %>

<a:messages all="true" filter="true"/>

<t:form method="login">
	<TABLE BORDER="0">
	<TR>
		<TD><a:key key="login.label.user"/></TD>
		<TD><t:inputText property="user" size="10" tabIndex="1"/></TD>
	</TR>
	<TR>
		<TD COLSPAN="2"><INPUT TYPE="submit" VALUE="<a:key key="login.label.next"/>"></TD>
	</TR>
	</TABLE>
</t:form>
<BR>
<BR>
<BR>
<t:link action="/currentLocate"><a:key key="list.label.locate"/></t:link>