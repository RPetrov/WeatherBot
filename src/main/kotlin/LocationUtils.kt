import kotlin.math.*

/**
 * Calculate distance between 2 points
 * If by your opinion Earth is flat you can use just Euclidean distance on your risk
 */
fun distance(
    latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double,
): Double {
    val R = 6371 // Radius of the Earth
    val latDistance = Math.toRadians(latitude2 - latitude1)
    val lonDistance = Math.toRadians(longitude2 - longitude1)
    val a = (sin(latDistance / 2) * sin(latDistance / 2)
            + (cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2))
            * sin(lonDistance / 2) * sin(lonDistance / 2)))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    var distance = R * c * 1000 // convert to meters
    val height = 0
    distance = distance.pow(2.0) + Math.pow(height.toDouble(), 2.0)
    return sqrt(distance)
}
