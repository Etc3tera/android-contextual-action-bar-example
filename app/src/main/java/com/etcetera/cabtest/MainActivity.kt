package com.etcetera.cabtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity(), ActionMode.Callback, SimpleAdapter.Event {
    private lateinit var mAdapter: SimpleAdapter
    private val items = mutableListOf<Alarm>()
    private var currentId = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (x in 0..10) {
            items.add(Alarm(x, false, false))
        }

        mAdapter = SimpleAdapter(items, this)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<RecyclerView>(R.id.listItem).also {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuAdd -> {
                items.add(Alarm(currentId++, false, false))
                mAdapter.notifyDataSetChanged()
                false
            }
            R.id.menuSelect -> {
                startActionMode(this)
                mAdapter.setActionMode(true)
                false
            }
            R.id.menuSelectAll -> {
                items.forEach { it.selected = true }
                startActionMode(this)
                mAdapter.setActionMode(true)
                false
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onActionItemClicked(mode: ActionMode, menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.menuCopy -> {
                Toast.makeText(this, "Copied ${items.filter{ it.selected }.map { it.id }.toList().joinToString() }", Toast.LENGTH_SHORT).show()
            }
            R.id.menuDelete -> {
                for(item in items.filter { it.selected }.map { it }.toList()){
                    items.remove(item)
                }
                Toast.makeText(this, "Deleted ${items.filter{ it.selected }.map { it.id }.toList().joinToString() }", Toast.LENGTH_SHORT).show()
                mAdapter.notifyDataSetChanged()
            }
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.context_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.title = "CAB"
        return false
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        Toast.makeText(this, "Closed CAB", Toast.LENGTH_SHORT).show()
        items.forEach { it.selected = false }
        mAdapter.setActionMode(false)
    }

    override fun onToggleAlarm(position: Int) {
        items[position].status = !items[position].status
        Toast.makeText(this, "Alarm ${items[position].id} is now ${if (items[position].status) "on" else "off"}", Toast.LENGTH_SHORT).show()
    }

    override fun onViewItem(position: Int) {
        Toast.makeText(this, "View detail on Alarm ${items[position].id}", Toast.LENGTH_SHORT).show()
    }
}
