<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="mega-tags" prefix="t" %>

<a:messages all="true" filter="true"/>

<t:form>
	<TABLE BORDER="0">
	<TR>
		<TD><a:key key="login.label.user"/></TD>
		<TD><t:inputText property="user" size="10" tabIndex="1"/></TD>
	</TR>
	<TR>
		<TD COLSPAN="2"><t:submitLink method="login"><a:key key="login.label.next"/></t:submitLink></TD>
	</TR>
	<TR>
		<t:submitHTML tag="td" style="border: 1px solid black;" method="login"><t:attribute name="colspan">2</t:attribute>Bute!</t:submitHTML>
	</TR>	
	</TABLE>
</t:form>
<BR>
<BR>
<BR>
<t:link action="/currentLocate"><a:key key="list.label.locate"/></t:link>