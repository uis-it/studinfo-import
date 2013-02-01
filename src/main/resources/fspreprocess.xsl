<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:fs="http://fsws.usit.no/schemas/studinfo"
    exclude-result-prefixes="xs"
    version="2.0">

    <!-- made with good help from http://stackoverflow.com/questions/12315353/xslt-transform-sequence-with-maxocurs-unbounded-to-nested-elements -->
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:namespace-alias stylesheet-prefix="#default" result-prefix="fs"/>
    
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
    
    <xsl:template match="fs:laringsutbytte">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <!-- This can be removed after the tag is renamed and all old studiebok tables are converted to PDF -->
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
    
    <xsl:template match="fs:abs_forkunnskaper_fritekst">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:anb_forkunnskaper">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:anb_forkunnskaper_fritekst">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:oblig_undakt_tilleggsinfo">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:praksis">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:apent_for_tillegg">
        <xsl:call-template name="freetext"/>
    </xsl:template>

    <!-- This can be removed after the tag is renamed and all studiebok tables are converted to PDF -->
    <xsl:template match="fs:åpent_for_tillegg">
        <xsl:call-template name="freetext">
            <xsl:with-param name="name" select="'apent_for_tillegg'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fs:beskrivelse">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:kortsam">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:p-formkrav">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:p-innhold">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:p-kortintr">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:p-profkval">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:p-studkval">
        <xsl:call-template name="freetext"/>
    </xsl:template>
    
    <xsl:template match="fs:sted">
        <xsl:element name="{@type}" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="child::*"/>
        </xsl:element>        
    </xsl:template>
    
    <xsl:template match="fs:list[@listType = 'numbered']">
        <xsl:element name="ol" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:list">
        <xsl:element name="ul" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:listItem">
        <xsl:element name="li" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:bold">
        <xsl:element name="b" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:italic">
        <xsl:element name="i" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="child::node()"/>
        </xsl:element>
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
    
    <xsl:template match="fs:meld_studentweb">
        <xsl:if test="text()">
            <xsl:copy>
                <xsl:value-of select="."/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="freetext">
        <xsl:param name="name" select="name()"/>
        <xsl:element name="{$name}" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
            <xsl:apply-templates select="child::node()|@*"/>
            <xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>