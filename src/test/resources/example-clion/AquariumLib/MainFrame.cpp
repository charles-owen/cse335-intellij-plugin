/**
 * @file MainFrame.cpp
 * @author Charles B. Owen
 */
#include "pch.h"
#include "ids.h"

#include "MainFrame.h"

#include "AquariumView.h"


/**
 * Constructor
 */
MainFrame::MainFrame() :
    wxFrame(NULL, wxID_ANY, L"Aquarium", wxDefaultPosition,  wxSize( 1000,800 ))
{
    this->SetSizeHints( 1000, 800 );

    wxBoxSizer* sizer;
    sizer = new wxBoxSizer( wxVERTICAL );

    mAquariumView = new AquariumView(this);
    sizer->Add(mAquariumView,1, wxEXPAND | wxALL, 5 );

    this->SetSizer( sizer );
    this->Layout();

    auto statusBar = this->CreateStatusBar( 1, wxSTB_SIZEGRIP, wxID_ANY );

    auto menuBar = new wxMenuBar( 0 );
    auto fileMenu = new wxMenu();
    auto helpMenu = new wxMenu();
    auto fishMenu = new wxMenu();

    fileMenu->Append(wxID_EXIT, "E&xit\tAlt-X", "Quit this program");
    helpMenu->Append(wxID_ABOUT, "&About\tF1", "Show about dialog");

    fishMenu->Append(IDM_ADDFISHBETA, L"&Beta Fish", L"Add a Beta Fish");
    fishMenu->Append(IDM_ADDFISHNEMO, L"&Nemo", L"Add Nemo");
    fishMenu->Append(IDM_ADDFISHANGEL, L"&Angel Fish", L"Add an Angel Fish");
    fishMenu->Append(IDM_ADDFISHCARP, L"&Killer Carp", L"Add a Killer Carp");

    Bind(wxEVT_COMMAND_MENU_SELECTED, &MainFrame::OnExit, this, wxID_EXIT);
    Bind(wxEVT_COMMAND_MENU_SELECTED, &MainFrame::OnAbout, this, wxID_ABOUT);

    menuBar->Append(fileMenu, L"&File" );
    menuBar->Append(fishMenu, L"&Add Fish");
    menuBar->Append(helpMenu, L"&Help");

    this->SetMenuBar( menuBar );
}

/**
 * Destructor
 */
MainFrame::~MainFrame()
{
}

/**
 * Exit menu option handlers
 * @param event
 */
void MainFrame::OnExit(wxCommandEvent& event)
{
  Close(true);
}

/**
 * Application about box menu handler
 */
void MainFrame::OnAbout(wxCommandEvent& WXUNUSED(event))
{
    wxMessageBox(L"Welcome to the Aquarium!",
            L"About Aquarium",
            wxOK,
            this);
}
