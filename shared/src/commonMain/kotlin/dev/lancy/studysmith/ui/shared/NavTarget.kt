package dev.lancy.studysmith.ui.shared

import com.bumble.appyx.navigation.node.LeafNode
import com.bumble.appyx.navigation.node.Node

/**
 * A [NavTarget] is an `enum` or a `sealed` class that represents navigation targets. All possible
 * targets must be known at compile time (excluding dynamic data).
 *
 * The `-Nav` suffix indicates that a node primarily serves to host / navigate to other nodes, and
 * are not intended to be used as a target for navigation. These are usually [Node]s.
 *
 * The `-Page` and `-Port` suffixes (standing for "viewport") indicates that a node is intended to
 * be used as a target for navigation, and is not intended to host other nodes. These are usually
 * [LeafNode]s.
 */
interface NavTarget
