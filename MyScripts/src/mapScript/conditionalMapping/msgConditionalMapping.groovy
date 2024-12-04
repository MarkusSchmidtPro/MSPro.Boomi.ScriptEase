package mapScript.conditionalMapping

import com.boomi.execution.ExecutionUtil

/** ==================================================================================
 * The script gets a comma separated list of VKORGs from a process property.
 * Then it checks if the current [VKORG] - provided as a map input - is
 * part of that list. 
 * If YES the output [region] is set to the input [REGION]
 * If NO the output [region] is empty.

 INPUT Parameters (DataContext):
 VKORG       : The current VKORG 
 REGION      : The current region.

 OUTPUT Parameters
 region 
 ----------------------------------------------------------------------------------
 2024-12-04  msc -   Created
 ==================================================================================
 */

// Give the script a meaningful name
// Start with msg -> Message Script Groovy
final String SCRIPT_NAME = "conditionalMapping"

// Get a logger instance that can be used to log messages 
def _logger = ExecutionUtil.getBaseLogger()
_logger.info('>>> Start Script ' + SCRIPT_NAME)

/*
 * Get the supported VKORG list from a process property,
 * and split into an String[].
 */
final String ppComponentId = "281434d2-f9d8-4700-905f-3acce983d3bf"
final String ppKeyVKOrgRegion = "7f244535-8805-4022-aab3-a636c035331e"
String vkOrgList = ExecutionUtil.getProcessProperty(ppComponentId, ppKeyVKOrgRegion) 
String[] orgs = vkOrgList.split( ",")

/* 
 * The script's logic is not magic, is it? 
 */
region = orgs.contains( VKORG ) ? REGIO : ""

/* Alternative way:
if (orgs.contains( VKORG )) {
    region = REGIO
} else {
    region = ""
}
*/

// Leave a footprint in the logs.
_logger.info("region($VKORG)='$region'")

