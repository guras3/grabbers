package com.example.twittergrabber

import com.example.core.domain.Message
import com.example.core.repositories.MessageRepository
import com.vaadin.annotations.Theme
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.tapio.googlemaps.GoogleMap
import com.vaadin.tapio.googlemaps.client.LatLon
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon
import com.vaadin.ui.*
import mu.KLogging
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import java.time.Duration
import java.util.*


@Theme("valo")
@SpringUI
class MapUI(private val messageRepository: MessageRepository) : UI() {
    private final val polygons = mutableSetOf<GoogleMapPolygon>()

    private companion object : KLogging() {
        val munuteInMillis = Duration.ofMinutes(1).toMillis()
    }

    override fun init(vaadinRequest: VaadinRequest?) {
        val consoleLayout = CssLayout()
        val console = Panel().also {
            it.setHeight("100px")
            it.content = consoleLayout
        }
        val mapContent = VerticalLayout().also {
            it.setSizeFull()
            val googleMap = GoogleMap("AIzaSyC5ib4FBnZDICe-nsjbGwpn9sNm6Z78jFw",
                    null, "russian")
            googleMap.setSizeFull()
            googleMap.center = LatLon(59.936265, 30.302409) //SPB
            googleMap.minZoom = 4
            googleMap.maxZoom = 16
            googleMap.addMapMoveListener({ zoomLevel, center, boundsNE, boundsSW ->
                updateMarkers(googleMap, boundsNE, boundsSW)
            })
            googleMap.addMarkerClickListener { clickedMarker ->
                val consoleEntry = Label("Marker \""
                        + clickedMarker.caption + "\" at ("
                        + clickedMarker.position.lat + ", "
                        + clickedMarker.position.lon + ") clicked.")
                consoleLayout.addComponent(consoleEntry, 0)
            }
            it.addComponent(googleMap)
            it.addComponent(console)
            it.setExpandRatio(googleMap, 1.0f)
        }

        val tabs = TabSheet().also {
            it.setSizeFull()
            it.addTab(mapContent, "Twi map")
            it.addTab(Label("An another tab"), "The other tab")
        }

        val rootLayout = CssLayout().also {
            it.setSizeFull()
            it.addComponent(tabs)
        }

        content = rootLayout
    }


    fun updateMarkers(googleMap: GoogleMap, boundsNE: LatLon, boundsSW: LatLon) {
        logger.debug { "${polygons.size} +++ $this" }
        val visibleMessages = messageRepository.findByCoordinatesWithin(
                GeoJsonPolygon(
                        Point(boundsSW.lon, boundsSW.lat),
                        Point(boundsSW.lon, boundsNE.lat),
                        Point(boundsNE.lon, boundsNE.lat),
                        Point(boundsNE.lon, boundsSW.lat),
                        Point(boundsSW.lon, boundsSW.lat) //close loop
                ),
                Date(System.currentTimeMillis() - (10 * munuteInMillis))
        )
        logger.info { "found ${visibleMessages.size} messages" }
        val (_, withoutExactLocation) = visibleMessages
                .partition { it.location.exactCoordinates != null }

        googleMap.clearMarkers()
        addMarkers(visibleMessages, googleMap)

        removeAllPolygonOverlays(googleMap)
        addPoligonOverlays(withoutExactLocation, googleMap)

    }

    private fun addMarkers(messages: List<Message>, googleMap: GoogleMap) {
        data class MsgWithLocation(val loc: LatLon, val msg: Message)
        messages
                .map {
                    val location = when {
                        it.location.exactCoordinates != null -> LatLon(it.location.exactCoordinates!!.y, it.location.exactCoordinates!!.x)
                        else -> {
                            val boundingBox = it.location.boundingBox!!.points
                            val lon = (boundingBox[0].x + boundingBox[3].x) / 2
                            val lat = (boundingBox[0].y + boundingBox[1].y) / 2
                            LatLon(lat, lon)
                        }
                    }
                    MsgWithLocation(location, it)
                }.groupBy({ it.loc }, { it.msg })
                .forEach { (loc, messages) ->
                    val allMsg = if (messages.size > 1) {
                        messages.mapIndexed { idx, msg -> "$idx) ${msg.text}" }.joinToString(separator = "\n")
                    } else {
                        messages.first().text
                    }

                    googleMap.addMarker(allMsg, loc, false, null)
                }
    }

    private fun removeAllPolygonOverlays(googleMap: GoogleMap) {
        polygons.forEach {
            googleMap.removePolygonOverlay(it)
        }
        polygons.clear()
    }

    private fun addPoligonOverlays(withPlace: List<Message>, googleMap: GoogleMap) {
        val newPolygons = withPlace.map { it.location.boundingBox }
                .distinct()
                .map {
                    GoogleMapPolygon(it!!.points.map { LatLon(it.y, it.x) }).also {
                        it.fillOpacity = 0.3
                    }
                }
        polygons.addAll(newPolygons)
        newPolygons.forEach {
            googleMap.addPolygonOverlay(it)
        }
    }
}