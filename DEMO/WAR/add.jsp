<%@ taglib uri="mega-action" prefix="a" %>
<%@ taglib uri="mega-tags" prefix="t" %>

<a:messages all="true" filter="true"/>

<t:form>
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
		<TD><a:key key="add.label.obs"/></TD>
		<TD><t:textarea property="obs" cols="50" rows="5"/></TD>
	</TR>	
	<TR>
		<TD><a:key key="add.label.admin"/></TD>
		<TD><t:checkBox property="admin" tabIndex="3"/></TD>
	</TR>	
		<TR>
		<TD><a:key key="add.label.xpto"/></TD>
		<TD>1 <t:multiCheck property="xpto">1</t:multiCheck><br/>
		2 <t:multiCheck property="xpto">2</t:multiCheck><br/>
		3 <t:multiCheck property="xpto">3</t:multiCheck><br/>
		4 <t:multiCheck property="xpto">4</t:multiCheck><br/>
		5 <t:multiCheck property="xpto">5</t:multiCheck></TD>
	</TR>	
	</TR>	
	<TR>
		<TD><a:key key="add.label.opcao"/></TD>
		<TD><t:listBox property="opcao" size="3"><t:options property="valores"/></t:listBox></TD>
	</TR>
	</TR>	
	<TR>
		<TD><a:key key="add.label.opcoes"/></TD>
		<TD><t:listBox property="opcoes" size="3"><t:options property="valores"/></t:listBox></TD>
	</TR>		
	<TR>
		<TD><a:key key="add.label.file"/></TD>
		<TD><t:file property="ficheiro"/></TD>
	</TR>	
	<TR>
		<TD COLSPAN="2"><t:submit method="insert"><a:key key="add.label.insert"/></t:submit></TD>
	</TR>
	</TABLE>
</t:form>

<BR/>

<t:link action="/db/recordList"><a:key key="add.label.back"/></t:link>
