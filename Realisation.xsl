<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
    <html>
      <head>
        <title>Export XML</title>
      </head>
      <body>
        <h1>Realisation.planner</h1>
        <pre>
          <xsl:copy-of select="*"/>
        </pre>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
