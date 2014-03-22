<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <!-- 
  <xsl:param name="fileName"/>
  <xsl:param name="updates" select="document($fileName)" />
   -->
  <xsl:param name="updates" />
  <xsl:variable name="updateItems" select="$updates/feed/entry" />
  
   
  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="feed">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()[not(self::entry)] | 
                                   entry[not(id = $updateItems/id)]" />
      <xsl:apply-templates select="$updateItems" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>