<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:fs="http://fsws.usit.no/schemas/studinfo"
    exclude-result-prefixes="xs"
    version="2.0">

    <!-- made with good help from http://stackoverflow.com/questions/12315353/xslt-transform-sequence-with-maxocurs-unbounded-to-nested-elements -->
    
    <xsl:output method="xml" encoding="UTF-8" indent="no"/>
    
    <xsl:namespace-alias stylesheet-prefix="#default" result-prefix="fs"/>
    
    <!--xsl:key name="fagperson-fields" match="fs:fagperson-liste/*[not(self::fs:personid)]" use="generate-id(preceding-sibling::fs:personid[1])"/-->

    <xsl:template match="/fs:fs-studieinfo">
        <xsl:copy copy-namespaces="yes">
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:attribute name="{local-name()}">
            <xsl:copy />
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="fs:intro">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:arbeidsformer">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:kvalifik_og_jobbmuligheter">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:opptaksinformasjon">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:utenlandsopphold">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:læringsutbytte">
        <xsl:call-template name="freetext">
            <xsl:with-param name="name" select="'laringsutbytte'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fs:fagplan_tekst">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:kontaktinformasjon">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:litteratur">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:innhold">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:opptakskrav">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:rammeplan_tekst">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:studentevaluering">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:vurdering_tilleggstekst">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:abs_forkunnskaper">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:anb_forkunnskaper">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:oblig_undakt_tilleggsinfo">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:praksis">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:åpent_for_tillegg">
        <xsl:call-template name="freetext">
            <xsl:with-param name="name" select="'apent_for_tillegg'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fs:beskrivelse">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:list">
        <xsl:variable name="listType">
            <xsl:choose>
                <xsl:when test="@listType = 'numbered'">ol</xsl:when>
                <xsl:otherwise>ul</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:element name="{$listType}">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:listItem">
        <xsl:element name="li">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:bold">
        <xsl:element name="b">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:italic">
        <xsl:element name="i">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:fagperson-liste">
        <xsl:element name="fagperson-liste" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:for-each-group select="*" group-starting-with="fs:personid">
                <xsl:element name="fagperson" namespace="http://fsws.usit.no/schemas/studinfo">
                    <xsl:apply-templates select="current-group()"/>
                </xsl:element>
            </xsl:for-each-group>
        </xsl:element>
        <!--xsl:element name="fagperson-liste">
            <xsl:apply-templates select="fs:personid"/>
        </xsl:element-->
    </xsl:template>
    
    <xsl:template match="fs:redregel">
        <xsl:element name="redregel" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="*[not(self::fs:emneid|self::fs:emnenavn)]"/>
            <xsl:for-each-group select="fs:emneid|fs:emnenavn" group-starting-with="fs:emneid">
                <xsl:element name="emne" namespace="http://fsws.usit.no/schemas/studinfo">
                    <xsl:apply-templates select="current-group()"/>
                </xsl:element>
            </xsl:for-each-group>
        </xsl:element>
    </xsl:template>
    
    <!--xsl:template match="fs:personid">
        <xsl:element name="person" inherit-namespaces="no">
            <xsl:element name="personid"><xsl:value-of select="."></xsl:value-of></xsl:element>
            <xsl:copy-of select="." copy-namespaces="no"/>
            <xsl:apply-templates select="key('fagperson-fields', generate-id())"/>
        </xsl:element>    
    </xsl:template-->
    
    <xsl:template name="freetext">
        <xsl:param name="name" select="name()"/>
        <xsl:element name="{$name}" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
            <xsl:apply-templates select="child::node()|@*"/>
            <xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>