<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="http://java.sun.com/jstl/core"   prefix="c" %>

<a:messages all="true" filter="true"/>

<TABLE BORDER="1">
	<TR>
		<TD><a:key key="list.label.id"/></TD>
		<TD><a:key key="list.label.name"/></TD>
		<TD>&nbsp;</TD>
	</TR>
<c:forEach var="elemento" items="${this.recordList}" varStatus="forEachStatus1">
	<TR>
		<TD><c:out value="${elemento.id}"/></TD>
		<TD><c:out value="${elemento.name}"/></TD>
		<TD>[<a href="<a:action method="delete"/>?id=<c:out value="${elemento.id}"/>"><a:key key="list.label.delete"/></a>]</TD>
	</TR>
</c:forEach>
</TABLE>

<BR/>

<a href="<a:action method="add"/>"><a:key key="list.label.addnew"/></a>


<BR/>
<BR/>
<a:key key="list.label.lastchange"/> <a:value property="lastChange"/>
<BR>
<BR>
<BR>
<a href="<a:action action="/currentLocate"/>"><a:key key="list.label.locate"/></a>
