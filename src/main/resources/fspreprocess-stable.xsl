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

    <xsl:template match="/fs:fs-studieinfo/fs:studieprogram">
        <xsl:copy copy-namespaces="yes">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="fs:studieprogramkode"/>
            <xsl:apply-templates select="fs:studieprogramnavn"/>
            <xsl:apply-templates select="fs:studieprogramnavn_vitn"/>
            <xsl:apply-templates select="fs:studieprogramnavn_en"/>
            <xsl:apply-templates select="fs:studieprogramnavn_vitn_en"/>
            <xsl:apply-templates select="fs:studiepoeng"/>
            <xsl:apply-templates select="fs:studieniva"/>
            <xsl:apply-templates select="fs:studienivakode"/>
            <xsl:apply-templates select="fs:studienivanavn"/>
            <xsl:apply-templates select="fs:sted[@type = 'fagansvarlig']"/>
            <xsl:apply-templates select="fs:sted[@type = 'adminansvarlig']"/>
            <xsl:apply-templates select="fs:gradnavn"/>
            <xsl:apply-templates select="fs:nuskode"/>
            <xsl:apply-templates select="fs:url"/>
            <xsl:apply-templates select="fs:finansieringsprosent"/>
            <xsl:apply-templates select="fs:heltidsprosent"/>
            <xsl:apply-templates select="fs:varighet"/>
            <xsl:apply-templates select="fs:grunnstudium"/>
            <xsl:apply-templates select="fs:kulltrinnplan"/>
            <xsl:apply-templates select="fs:arbeidsformer"/>
            <xsl:apply-templates select="fs:fagplan_tekst"/>
            <xsl:apply-templates select="fs:fagplan_URL"/>
            <xsl:apply-templates select="fs:innhold"/>
            <xsl:apply-templates select="fs:intro"/>
            <xsl:apply-templates select="fs:kontaktinformasjon"/>
            <xsl:apply-templates select="fs:kvalifik_og_jobbmuligheter"/>
            <xsl:apply-templates select="fs:læringsutbytte"/>
            <xsl:apply-templates select="fs:litteratur"/>
            <xsl:apply-templates select="fs:opptaksinformasjon"/>
            <xsl:apply-templates select="fs:opptakskrav"/>
            <xsl:apply-templates select="fs:p-formkrav"/>
            <xsl:apply-templates select="fs:p-innhold"/>
            <xsl:apply-templates select="fs:p-kortintr"/>
            <xsl:apply-templates select="fs:p-profkval"/>
            <xsl:apply-templates select="fs:p-studkval"/>
            <xsl:apply-templates select="fs:se-introd"/>
            <xsl:apply-templates select="fs:rammeplan_tekst"/>
            <xsl:apply-templates select="fs:rammeplan_URL"/>
            <xsl:apply-templates select="fs:studentevaluering"/>
            <xsl:apply-templates select="fs:utenlandsopphold"/>
            <xsl:apply-templates select="fs:vurdering_tilleggstekst"/>
            <xsl:apply-templates select="fs:opptak"/>
            <xsl:apply-templates select="fs:fagkode"/>
            <xsl:apply-templates select="fs:sprak-liste"/>
            <xsl:apply-templates select="fs:utdanningsplan"/>
            <xsl:apply-templates select="fs:planforslag"/>
            <xsl:apply-templates select="fs:termin-siste-opptak"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="/fs:fs-studieinfo/fs:emne">
        <xsl:copy copy-namespaces="yes">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="fs:emneid"/>
            <xsl:apply-templates select="fs:emnenavn"/>
            <xsl:apply-templates select="fs:emnenavn_en"/>
            <xsl:apply-templates select="fs:studiepoeng"/>
            <xsl:apply-templates select="fs:status-privatist"/>
            <xsl:apply-templates select="fs:studieniva"/>
            <xsl:apply-templates select="fs:nuskode"/>
            <xsl:apply-templates select="fs:enkeltemneopptak"/>
            <xsl:apply-templates select="fs:studierettkrav"/>
            <xsl:apply-templates select="fs:status_oblig"/>
            <xsl:apply-templates select="fs:obligund"/>
            <xsl:apply-templates select="fs:undervisningssemester"/>
            <xsl:apply-templates select="fs:antall-undsemester"/>
            <xsl:apply-templates select="fs:antall-forelesningstimer"/>
            <xsl:apply-templates select="fs:antall-selvstudiumtimer"/>
            <xsl:apply-templates select="fs:undsemester"/>
            <xsl:apply-templates select="fs:emnetype"/>
            <xsl:apply-templates select="fs:url"/>
            <xsl:apply-templates select="fs:periode-eks"/>
            <xsl:apply-templates select="fs:periode-und"/>
            <xsl:apply-templates select="fs:eksamenssemester"/>
            <xsl:apply-templates select="fs:anbefalte-forkunnskaper"/>
            <xsl:apply-templates select="fs:formelle-forkunnskaper"/>
            <xsl:apply-templates select="fs:inngar-i-studieprogram"/>
            <xsl:apply-templates select="fs:inngar-i-fag"/>
            <xsl:apply-templates select="fs:sted[@type = 'fagansvarlig']"/>
            <xsl:apply-templates select="fs:sted[@type = 'adminansvarlig']"/>
            <xsl:apply-templates select="fs:sprak-liste"/>
            <xsl:apply-templates select="fs:fagperson-liste"/>
            <xsl:apply-templates select="fs:dato-eksamen"/>
            <xsl:apply-templates select="fs:eksamensordning"/>
            <xsl:apply-templates select="fs:vurdordning"/>
            <xsl:apply-templates select="fs:vektingsreduksjon"/>
            <xsl:apply-templates select="fs:abs_forkunnskaper_fritekst"/>
            <xsl:apply-templates select="fs:anb_forkunnskaper_fritekst"/>
            <xsl:apply-templates select="fs:arbeidsformer"/>
            <xsl:apply-templates select="fs:innhold"/>
            <xsl:apply-templates select="fs:intro"/>
            <xsl:apply-templates select="fs:kortsam"/>
            <xsl:apply-templates select="fs:kvalifik_og_jobbmuligheter"/>
            <xsl:apply-templates select="fs:litteratur"/>
            <xsl:apply-templates select="fs:læringsutbytte"/>
            <xsl:apply-templates select="fs:oblig_undakt_tilleggsinfo"/>
            <xsl:apply-templates select="fs:opptakskrav"/>
            <xsl:apply-templates select="fs:praksis"/>
            <xsl:apply-templates select="fs:studentevaluering"/>
            <xsl:apply-templates select="fs:vurdering_tilleggstekst"/>
            <xsl:apply-templates select="fs:åpent_for_tillegg"/>
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
    
    <xsl:template match="fs:se-introd">
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