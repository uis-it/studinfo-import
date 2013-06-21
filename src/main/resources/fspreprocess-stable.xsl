<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:fs="http://fsws.usit.no/schemas/studinfo"
    exclude-result-prefixes="xs"
    version="2.0">

    <!-- made with good help from http://stackoverflow.com/questions/12315353/xslt-transform-sequence-with-maxocurs-unbounded-to-nested-elements -->
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:namespace-alias stylesheet-prefix="#default" result-prefix="fs"/>
    
    <xsl:template match="/fs:fs-studieinfo">
        <xsl:element name="fs-studieinfo" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:namespace  name="xsi" select="'http://www.w3.org/2001/XMLSchema-instance'"/>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/fs:fs-studieinfo/fs:studieprogram">
        <xsl:element name="studieprogram" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@sprak"/>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramnavn_vitn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramnavn_en'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramnavn_vitn_en'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieniva'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studienivakode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studienivanavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagansvarlig'"/>
                <xsl:with-param name="elem-node" select="fs:sted[@type = 'fagansvarlig']"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'adminansvarlig'"/>
                <xsl:with-param name="elem-node" select="fs:sted[@type = 'adminansvarlig']"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'gradnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'nuskode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'url'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'finansieringsprosent'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'heltidsprosent'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'varighet'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'grunnstudium'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kulltrinnplan'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'arbeidsformer'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagplan_tekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagplan_URL'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'innhold'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'intro'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kontaktinformasjon'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kvalifik_og_jobbmuligheter'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'laringsutbytte'"/>
                <xsl:with-param name="elem-node" select="fs:læringsutbytte"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'litteratur'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'opptaksinformasjon'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'opptakskrav'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'p-formkrav'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'p-innhold'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'p-kortintr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'p-profkval'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'p-studkval'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'se-introd'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'rammeplan_tekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'rammeplan_URL'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studentevaluering'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'utenlandsopphold'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdering_tilleggstekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'opptak'"/>
                <xsl:with-param name="nodes" select="fs:opptak"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'sprak'"/>
                <xsl:with-param name="nodes" select="fs:sprak-liste/fs:sprak"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'utdanningsplan'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'planforslag'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-siste-opptak'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="/fs:fs-studieinfo/fs:emne">
        <xsl:element name="emne" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@sprak"/>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emneid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnenavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnenavn_en'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'status-privatist'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieniva'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'nuskode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'enkeltemneopptak'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studierettkrav'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'status_oblig'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'obligund'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undervisningssemester'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'antall-undsemester'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'antall-forelesningstimer'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'antall-selvstudiumtimer'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'undsemester'"/>
                <xsl:with-param name="nodes" select="fs:undsemester"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnetype'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'url'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'periode-eks'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'periode-und'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamenssemester'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'anbefalte-forkunnskaper'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'formelle-forkunnskaper'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'inngar-i-studieprogram'"/>
                <xsl:with-param name="nodes" select="fs:inngar-i-studieprogram"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'inngar-i-fag'"/>
                <xsl:with-param name="nodes" select="fs:inngar-i-fag/fs:fagkode"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagansvarlig'"/>
                <xsl:with-param name="elem-node" select="fs:sted[@type = 'fagansvarlig']"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'adminansvarlig'"/>
                <xsl:with-param name="elem-node" select="fs:sted[@type = 'adminansvarlig']"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'sprak'"/>
                <xsl:with-param name="nodes" select="fs:sprak-liste/fs:sprak"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'fagperson'"/>
                <xsl:with-param name="nodes" select="fs:fagperson-liste/fs:fagperson"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-eksamen'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'eksamensordning'"/>
                <xsl:with-param name="nodes" select="fs:eksamensordning"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'vurdordning'"/>
                <xsl:with-param name="nodes" select="fs:vurdordning"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vektingsreduksjon'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'abs_forkunnskaper_fritekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'anb_forkunnskaper_fritekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'arbeidsformer'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'innhold'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'intro'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kortsam'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kvalifik_og_jobbmuligheter'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'litteratur'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'laringsutbytte'"/>
                <xsl:with-param name="elem-node" select="fs:læringsutbytte"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'oblig_undakt_tilleggsinfo'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'opptakskrav'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'praksis'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studentevaluering'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdering_tilleggstekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'apent_for_tillegg'"/>
                <xsl:with-param name="elem-node" select="fs:åpen_for_tillegg"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="/fs:fs-studieinfo/fs:kurs">
        <xsl:element name="kurs" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@sprak"/>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kursid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kursnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagansvarlig'"/>
                <xsl:with-param name="elem-node" select="fs:sted[@type = 'fagansvarlig']"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'adminansvarlig'"/>
                <xsl:with-param name="elem-node" select="fs:sted[@type = 'adminansvarlig']"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-opptak-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-opptak-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-frist-soknad'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'url'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'varighettimer'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'email'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fjernundervisning'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'desentral-undervisning'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'nettbasert-undervisning'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kan-tilbys'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'skal-avholdes'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kostnadkommentar'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'kurskategori'"/>
                <xsl:with-param name="nodes" select="fs:kurskategori-liste"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-publiseres-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-publiseres-til'"/>
            </xsl:call-template>
        </xsl:element>            
    </xsl:template>

    <xsl:template match="fs:kurs/fs:kursid">
        <xsl:element name="kursid" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kurskode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'tidkode'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:kurs/fs:kurskategori-liste">
        <xsl:element name="kurskategori" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kurskategorikode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'kurskategorinavn'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:studieprogram/fs:opptak">
        <xsl:element name="opptak" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'opptakstype'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'opptakskode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'frist_soknad'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'frist_ettersending'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:emneid">
        <xsl:element name="emneid" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'institusjonsnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnekode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'versjonskode'"/>
            </xsl:call-template>
        </xsl:element>            
    </xsl:template>
    
    <xsl:template match="fs:emne/fs:periode-und | fs:emne/fs:periode-eks">
        <xsl:element name="{name()}" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'forstegang'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sistegang'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:personnavn">
        <xsl:element name="personnavn" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'etternavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fornavn'"/>
            </xsl:call-template>
        </xsl:element>
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
    
    <xsl:template match="fs:obligund">
        <xsl:element name="obligund" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'obligoppgave'"/>
            </xsl:call-template>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:obligoppgave">
        <xsl:element name="obligoppgave" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@nr"/>
            <xsl:apply-templates select="text()"/>
        </xsl:element>        
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
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'institusjonsnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fakultetsnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'instituttnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'gruppenr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'navn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'avdnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'url'"/>
            </xsl:call-template>
        </xsl:element>        
    </xsl:template>
    
    <xsl:template match="fs:fagperson">
        <xsl:element name="fagperson" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'personid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'personrolle'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'personnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-til'"/>
            </xsl:call-template>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:termin">
        <xsl:element name="termin" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'terminnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'terminnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'generer_und'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:undervisningstermin">
        <xsl:element name="undervisningstermin" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@start"/>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'termin'"/>
                <xsl:with-param name="nodes" select="fs:termin"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:undsemester">
        <xsl:element name="undsemester" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'semester'"/>
                <xsl:with-param name="nodes" select="fs:semester"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'forstegang'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sistegang'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:undsemester/fs:semester">
        <xsl:element name="semester" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@nr"/>
            <xsl:apply-templates select="text()"/>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:sprak-liste/fs:sprak">
        <xsl:element name="sprak" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sprakkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undervisning'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdering'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamen'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:utdanningsplan">
        <xsl:element name="utdanningsplan" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'planinformasjon'"/>
                <xsl:with-param name="nodes" select="fs:planinformajson"/>
            </xsl:call-template>
            
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'krav-sammensetting'"/>
                <xsl:with-param name="nodes" select="fs:krav-sammensetting"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:vurdenhet">
        <xsl:element name="vurdenhet" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'tid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'tid-reell'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdstatus'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-eksamen'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'klokkeslett-eksamen-fremmotetid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-uttak'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'klokkeslett-uttak'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-innlevering'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'klokkeslett-innlevering'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:planinformasjon">
        <xsl:element name="planelement" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-gjelder-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'infotekst'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'url'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:krav-sammensetting">
        <xsl:element name="krav-sammensetting" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'arstall-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'terminkode-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnekombinasjon'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:emnekombinasjon">
        <xsl:element name="emnekombinasjon" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@niva"/>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnekombinasjonskode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnekombinasjonsnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'beskrivelse'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieretning'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng-krav'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'terminnr-relativ-start'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-start-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-start-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fargekode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'veivalg'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'valg-alle-emner'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'opptak'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'emne'"/>
                <xsl:with-param name="nodes" select="fs:emne"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'planelement'"/>
                <xsl:with-param name="nodes" select="fs:planelement"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'emnekombinasjon-valg'"/>
                <xsl:with-param name="nodes" select="fs:emnekombinasjon-valg"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'emnekombinasjon-frievalg'"/>
                <xsl:with-param name="nodes" select="fs:emnekombinasjon-frievalg"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'emnekombinasjon'"/>
                <xsl:with-param name="nodes" select="fs:emnekombinasjon"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:emnekombinasjon/fs:emne">
        <xsl:element name="emne" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emneid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnenavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'undervisningstermin'"/>
                <xsl:with-param name="nodes" select="fs:undervisningstermin"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sorteringsrekkefolge'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'valgstatus'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'valgstatusnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'valgstatusbeskrivelse'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'valgstatusbeskrivelse-en'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undtermin-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undtermin-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undtermin-default'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-start-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-start-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-valg-til'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:planelement">
        <xsl:element name="planelement" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'planelementkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'planelementnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'planelementnavn-en'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sorteringsrekkefolge'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'valgstatus'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undtermin-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undtermin-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'url'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:planforslag">
        <xsl:element name="planforslag" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'beskrivelse'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnekombinasjonsnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-start'"/>
            </xsl:call-template>
            <xsl:element name="planemne-liste">
                <xsl:call-template name="elem">
                    <xsl:with-param name="elem-name" select="'emne'"/>
                </xsl:call-template>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:planforslag/fs:emne">
        <xsl:element name="emne" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emneid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnenavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'undtermin'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:emnekombinasjon-frievalg">
        <xsl:element name="emnekombinasjon-frievalg" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'lopenr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'institusjonsnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fakultetsnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'instituttnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'gruppenr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studienivakode-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studienivakode-til'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studierettkrav'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:emnekombinasjon-valg">
        <xsl:element name="emnekombinasjon-valg" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'arstall-kull'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'terminkode-kull'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-start'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-frist'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:eksamensdag">
        <xsl:element name="eksamensdag" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamensdagnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng-reduksjon'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'varighet-timer'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'lovlig-hjelpemiddel'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:eksamensdel">
        <xsl:element name="eksamensdel" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamensdelnr'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamensdelnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng-reduksjon'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'varighet'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamensform'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'karaktervekt'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'karregel'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'eksamensdag'"/>
                <xsl:with-param name="nodes" select="fs:eksamensdag"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-eksamen'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'lovlig-hjelpemiddel'"/>
                <xsl:with-param name="nodes" select="fs:lovlig-hjelpemiddel"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:lovlig-hjelpemiddel/fs:hjelpemiddel">
        <xsl:element name="hjelpemiddel" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'hjelpemiddelnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'hjelpemiddelmerknad'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:vurdkombinasjon">
        <xsl:element name="vurdkombinasjon" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:apply-templates select="@niva"/>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdkombkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdkombnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdform'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdkombtype'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'forstegang'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sistegang'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'brok'"/>
                <xsl:with-param name="default-value" select="'1/1'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'varighet'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdering'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'alle-kandidater'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'karregelkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'karregel'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'vurdkombtid'"/>
                <xsl:with-param name="wrapper-elem-name" select="'vurdkombtider'"/>
                <xsl:with-param name="nodes" select="fs:vurdkombtider/fs:vurdkombtid"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'hjelpemiddel'"/>
                <xsl:with-param name="nodes" select="fs:lovlig-hjelpemiddel/fs:hjelpemiddel"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'tid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'tid-reell'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdstatus'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-uttak'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'klokkeslett-uttak'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-innlevering'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'klokkeslett-innlevering'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-eksamen'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'klokkeslett-eksamen-fremmotetid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'vurdenhet'"/>
                <xsl:with-param name="wrapper-elem-name" select="'vurdenheter'"/>
                <xsl:with-param name="nodes" select="fs:vurdenheter/fs:vurdenhet"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'vurdkombinasjon'"/>
                <xsl:with-param name="nodes" select="fs:vurdkombinasjon"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template match="fs:vurdkombtid">
        <xsl:element name="vurdkombtid" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdtidkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdtidkode_reell'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'arsinkrement'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdstatuskode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'liketallsaar'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'oddetallsaar'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'forstegang'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'sistegang'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:vurdordning">
        <xsl:element name="vurdordning" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdordningid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdordningnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'default'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'meld_studentweb'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'vurdkombinasjon'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:eksamensordning">
        <xsl:element name="eksamensordning" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamensordningid'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'eksamensordningnavn'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'default'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'meld_studentweb'"/>
                <xsl:with-param name="default-value" select="'N'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'hjelpemiddel'"/>
                <xsl:with-param name="nodes" select="fs:lovlig-hjelpemiddel/fs:hjelpemiddel"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'karregel'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'dato-eksamen'"/>
            </xsl:call-template>
            <xsl:call-template name="elem-array">
                <xsl:with-param name="elem-name" select="'eksamensdel'"/>
                <xsl:with-param name="nodes" select="fs:eksamensdel"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:inngar-i-fag">
        <xsl:element name="inngar-i-fag" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fagkode'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:inngar-i-studieprogram">
        <xsl:element name="inngar-i-studieprogram" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramkode'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studieprogramnavn'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:inngar-i-fag/fs:fagkode">
        <xsl:element name="inngar-i-fag" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:element name="fagkode" namespace="http://fsws.usit.no/schemas/studinfo">
                <xsl:value-of select="text()"/>
            </xsl:element>
        </xsl:element>    
    </xsl:template>
    
    <xsl:template match="fs:kravsammensetting">
        <xsl:element name="kravSammensetting" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'termin-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'arstall-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'terminkode-gjelder-fra'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'emnekombinasjon'"/>
            </xsl:call-template>
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
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'studiepoeng-reduksjon'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'periode'"/>
            </xsl:call-template>
            <xsl:element name="redregel-emne-liste" namespace="http://fsws.usit.no/schemas/studinfo">
                <xsl:for-each-group select="fs:emneid|fs:emnenavn" group-starting-with="fs:emneid">
                    <xsl:element name="emne" namespace="http://fsws.usit.no/schemas/studinfo">
                        <xsl:apply-templates select="current-group()"/>
                    </xsl:element>
                </xsl:for-each-group>
            </xsl:element>            
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="fs:redregel/fs:periode">
        <xsl:element name="periode" namespace="http://fsws.usit.no/schemas/studinfo">
            <!-- type Datoperiode -->
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'fradato'"/>
            </xsl:call-template>
            <xsl:call-template name="elem">
                <xsl:with-param name="elem-name" select="'tildato'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    
    <xsl:template name="elem">
        <!-- This template makes sure the node is created even if doesn't exist in the source document-->
        <xsl:param name="elem-name"/>
        <xsl:param name="elem-node" select="child::*[local-name() = $elem-name]"/>
        <xsl:param name="default-value" />
        
        <xsl:choose>
            <xsl:when test="(empty($elem-node) or empty($elem-node/child::*)) and empty($elem-node/text())">
                <xsl:element name="{$elem-name}" namespace="http://fsws.usit.no/schemas/studinfo">
                    <xsl:choose>
                        <xsl:when test="string-length($default-value) = 0">
                            <xsl:attribute name="xsi:nil" select="true()"></xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$default-value"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:element>    
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="$elem-node"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="elem-array">
        <xsl:param name="elem-name"/>
        <xsl:param name="wrapper-elem-name" select="concat($elem-name, '-liste')"/>
        <xsl:param name="nodes"/>
        <xsl:element name="{$wrapper-elem-name}" namespace="http://fsws.usit.no/schemas/studinfo">
            <xsl:if test="exists($nodes)">
                <xsl:call-template name="elem">
                    <xsl:with-param name="elem-name" select="$elem-name"/>
                    <xsl:with-param name="elem-node" select="$nodes"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:element>
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